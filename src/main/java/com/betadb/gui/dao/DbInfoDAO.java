package com.betadb.gui.dao;

import com.betadb.gui.dbobjects.Column;
import com.betadb.gui.dbobjects.DbInfo;
import com.betadb.gui.dbobjects.DbObject;
import com.betadb.gui.dbobjects.DbObjectType;
import com.betadb.gui.dbobjects.ForeignKey;
import com.betadb.gui.dbobjects.Index;
import com.betadb.gui.dbobjects.Parameter;
import com.betadb.gui.dbobjects.PrimaryKey;
import com.betadb.gui.dbobjects.Procedure;
import com.betadb.gui.dbobjects.Table;
import com.betadb.gui.dbobjects.View;
import com.betadb.gui.exception.BetaDbException;
import static com.betadb.gui.jdbc.util.ResultSetUtils.getRowAsProperties;
import com.betadb.gui.script.ScriptUtils;
import com.betadb.gui.util.Pair;
import static com.google.common.collect.ArrayListMultimap.create;
import com.google.common.collect.ListMultimap;
import com.google.common.util.concurrent.ListeningExecutorService;
import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.util.concurrent.Executors.newFixedThreadPool;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import static java.util.logging.Logger.getLogger;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * @author parmstrong
 */
public class DbInfoDAO
{
	DataSource ds;
	ListeningExecutorService threadPool;

	//add query from sys.system_objects, sys.system_columns for system views and stored procs
	public DbInfoDAO(DataSource ds)
	{
		this.ds = ds;
		this.threadPool = listeningDecorator(newFixedThreadPool(10));
	}

	public List<DbInfo> getDatabases() throws SQLException
	{
		List<DbInfo> retVal;
		try (Connection conn = ds.getConnection())
		{
			ResultSet rs = conn.getMetaData().getCatalogs();
			retVal = new ArrayList<>();
			while (rs.next())
			{
				DbInfo dbInfo = new DbInfo(rs.getString(1));
				retVal.add(dbInfo);
			}	rs.close();
		}

		return retVal;
	}

	public DbInfo refreshDbInfo(DbInfo info) throws SQLException
	{
		try
		{
			threadPool.submit(new ProcedureLoader(info));
			threadPool.submit(new TableLoader(info));
			threadPool.shutdown();
			threadPool.awaitTermination(30, TimeUnit.SECONDS);
			info.setLazyDataLoaded(true);
			return info;
		}
		catch (InterruptedException ex)
		{
			throw new BetaDbException("Error loading db objects");
		}
	}

	private class ProcedureLoader implements Runnable
	{
		private DbInfo dbInfo;

		public ProcedureLoader(DbInfo dbInfo)
		{
			this.dbInfo = dbInfo;
		}

		@Override
		public void run()
		{
			Connection conn;
			try
			{
				conn = ds.getConnection();
				DatabaseMetaData metaData = conn.getMetaData();       
				ListMultimap<DbObjectKey, Parameter> procedureParameters = getProcedureParameters(metaData, dbInfo.getDbName());
				List<Procedure> procedures = getProcedures(metaData, dbInfo.getDbName(), procedureParameters);
				dbInfo.setProcedures(procedures);
			}
			catch (SQLException ex)
			{
				getLogger(DbInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private class TableLoader implements Runnable
	{
		private DbInfo dbInfo;

		public TableLoader(DbInfo dbInfo)
		{
			this.dbInfo = dbInfo;
		}

		@Override
		public void run()
		{
			Connection conn;
			try
			{
				conn = ds.getConnection();
				DatabaseMetaData metaData = conn.getMetaData();
				ListMultimap<DbObjectKey, Column> columns = getColumns(metaData, dbInfo.getDbName());
				Pair<List<Table>, List<View>> tablesAndViewsPair = getTablesAndViews(metaData, dbInfo.getDbName(), columns);
				dbInfo.setTables(tablesAndViewsPair.getFirst());
				dbInfo.setViews(tablesAndViewsPair.getSecond());
			}
			catch (SQLException ex)
			{
				getLogger(DbInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private Pair<List<Table>, List<View>> getTablesAndViews(DatabaseMetaData databaseMetaData, String dbName, ListMultimap<DbObjectKey, Column> columns) throws SQLException
	{
		List<Table> tables = new ArrayList<>();
		List<View> views = new ArrayList<>();

		ResultSet rs = databaseMetaData.getTables(dbName, null, null, null);

		while (rs.next())
		{
			Table table = new Table();
			table.setObjectType(DbObjectType.VIEW);
			if ("VIEW".equals(rs.getString("TABLE_TYPE")))
			{
				View view = new View();
				view.setObjectType(DbObjectType.VIEW);
				views.add(view);
				table = view;
			}
			else
				tables.add(table);
			table.setSchemaName(rs.getString("TABLE_SCHEM"));
			table.setName(rs.getString("TABLE_NAME"));
			table.setProperties(getRowAsProperties(rs));
			table.setColumns(columns.get(new DbObjectKey(dbName, table.getSchemaName(), table.getName())));
		}

		return new Pair<>(tables, views);
	}

	private ListMultimap<DbObjectKey, Column> getColumns(DatabaseMetaData databaseMetaData, String catalog) throws SQLException
	{
		ListMultimap<DbObjectKey, Column> retVal = create();

		ResultSet rs = databaseMetaData.getColumns(catalog, null, null, null);
		while (rs.next())
		{
			Column c = new Column();
			c.setObjectType(DbObjectType.COLUMN);
			c.setName(rs.getString("COLUMN_NAME"));
			c.setSchemaName(rs.getString("TABLE_SCHEM"));
			c.setDecimalDigits(rs.getInt("COLUMN_SIZE"));
			c.setDataType(rs.getString("TYPE_NAME"));
			c.setProperties(getRowAsProperties(rs));
			retVal.put(new DbObjectKey(catalog, rs.getString("TABLE_SCHEM"), rs.getString("TABLE_NAME")), c);
		}
		return retVal;
	}
    
//    private ListMultimap<DbObjectKey, Column> getColumns(Table table, String catalog) throws SQLException
//	{
//		ListMultimap<DbObjectKey, Column> retVal = create();
//
//		ResultSet rs = databaseMetaData.getColumns(catalog, table.getSchemaName(), table.getName(), null);
//		while (rs.next())
//		{
//			Column c = new Column();
//			c.setObjectType(DbObjectType.COLUMN);
//			c.setName(rs.getString("COLUMN_NAME"));
//			c.setSchemaName(rs.getString("TABLE_SCHEM"));
//			c.setDecimalDigits(rs.getInt("COLUMN_SIZE"));
//			c.setDataType(rs.getString("TYPE_NAME"));
//			c.setProperties(getRowAsProperties(rs));
//			retVal.put(new DbObjectKey(catalog, rs.getString("TABLE_SCHEM"), rs.getString("TABLE_NAME")), c);
//		}
//		return retVal;
//	}
    


	private List<Procedure> getProcedures(DatabaseMetaData databaseMetaData, String dbName, ListMultimap<DbObjectKey, Parameter> parameterMap) throws SQLException
	{
		List<Procedure> results = new ArrayList<>();

		ResultSet rs = databaseMetaData.getProcedures(dbName, null, null);
		while (rs.next())
		{
			Procedure procedure = new Procedure();
			String name = rs.getString("PROCEDURE_NAME");
			procedure.setObjectType(DbObjectType.PROCEDURE);
			procedure.setName(cleanUpName(name));
			procedure.setSchemaName(rs.getString("PROCEDURE_SCHEM"));
			procedure.setProperties(getRowAsProperties(rs));
			procedure.setParameters(parameterMap.get(new DbObjectKey(dbName, procedure.getSchemaName(), procedure.getName())));
			results.add(procedure);
		}

		return results;
	}

	//here if the name has a semicolon it is a grouping for jtds and should be removed this is already what jtds does with ;1's at the end of a name
	//but we have some ;0's
	private String cleanUpName(String name)
	{
		int semiColonIndex = name.indexOf(";");
		if (semiColonIndex >= 0)
			name = name.substring(0, semiColonIndex);
		return name;
	}

	private ListMultimap<DbObjectKey, Parameter> getProcedureParameters(DatabaseMetaData metaData, String dbName) throws SQLException
	{
		ResultSet rs = metaData.getProcedureColumns(dbName, null, null, null);
		ListMultimap<DbObjectKey, Parameter> parameterMap = create();

		while (rs.next())
		{
			if (rs.getShort("COLUMN_TYPE") != 1)
				continue;
			Parameter parameter = new Parameter();
			parameter.setObjectType(DbObjectType.PARAMETER);
			parameter.setName(rs.getString("COLUMN_NAME"));
			parameter.setSchemaName(rs.getString("PROCEDURE_SCHEM"));
			parameter.setProperties(getRowAsProperties(rs));
			parameterMap.put(new DbObjectKey(dbName, parameter.getSchemaName(), rs.getString("PROCEDURE_NAME")), parameter);
		}
		return parameterMap;
	}

	public String getScript(DbObject dbObject, String dbName) throws SQLException
	{
		if(dbObject.getObjectType().equals(DbObjectType.TABLE))
			return ScriptUtils.scriptTable((Table)dbObject);

		String sql = "use " + dbName + "; exec sp_helpText '" + dbObject.getSchemaName() + "." + dbObject.getName() + "'";
		QueryRunner runner = new QueryRunner(ds);
		List<String> results = (List) runner.query(sql, new ColumnListHandler());
		return join(results, "");
	}

	public List<Index> getIndexes(String dbName, Table table) throws SQLException
	{
		List<Index> indexes = new ArrayList<>();
		Connection conn;
		try
		{
			conn = ds.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getIndexInfo(dbName, table.getSchemaName(), table.getName(), false, false);
			while (rs.next())
			{
				Index index = new Index();
				index.setObjectType(DbObjectType.INDEX);
				index.setName(rs.getString("Index_Name"));
				index.setSchemaName(rs.getString("TABLE_SCHEM"));
				index.setProperties(getRowAsProperties(rs));
				indexes.add(index);
			}			
		}
		catch (SQLException ex)
		{
			getLogger(DbInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
		}

		return indexes;
	}

	public List<ForeignKey> getForeignKeys(String dbName, Table table) throws SQLException
	{
		List<ForeignKey> foreignKeys = new ArrayList<>();
		Connection conn;
		try
		{
			conn = ds.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getImportedKeys(dbName, table.getSchemaName(), table.getName());
			while (rs.next())
			{
				ForeignKey foreignKey = new ForeignKey();
				foreignKey.setObjectType(DbObjectType.FOREIGN_KEY);
				foreignKey.setName(rs.getString("FK_NAME"));
				foreignKey.setSchemaName(table.getSchemaName());
				foreignKey.setProperties(getRowAsProperties(rs));
				foreignKeys.add(foreignKey);
			}
		}
		catch (SQLException ex)
		{
			getLogger(DbInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
		}

		return foreignKeys;
	}

	public List<PrimaryKey> getPrimaryKeys(String dbName, Table table) throws SQLException
	{
		List<PrimaryKey> primaryKeys = new ArrayList<>();
		Connection conn;
		try
		{
			conn = ds.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getPrimaryKeys(dbName, table.getSchemaName(), table.getName());
			while (rs.next())
			{
				PrimaryKey primaryKey = new PrimaryKey();
				primaryKey.setObjectType(DbObjectType.PRIMARY_KEY);
				primaryKey.setName(rs.getString("PK_NAME"));
				primaryKey.setSchemaName(table.getSchemaName());
				primaryKey.setProperties(getRowAsProperties(rs));
				primaryKeys.add(primaryKey);
			}
		}
		catch (SQLException ex)
		{
			getLogger(DbInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
		}

		return primaryKeys;
	}


	public List<Map<String,String>> getTablePrivileges(String dbName, Table table) throws SQLException
	{
		List<Map<String,String>> tablePrivileges = new ArrayList<>();
		Connection conn;
		try
		{
			conn = ds.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getTablePrivileges(dbName, table.getSchemaName(), table.getName());
			while (rs.next())
				tablePrivileges.add(getRowAsProperties(rs));
		}
		catch (SQLException ex)
		{
			getLogger(DbInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return tablePrivileges;
	}

	private class DbObjectKey
	{
		private String schema;
		private String catalog;
		private String name;

		public DbObjectKey(String catalog, String schema, String name)
		{
			this.schema = schema;
			this.catalog = catalog;
			this.name = name;
		}

		@Override
		public int hashCode()
		{
			int hash = 5;
			hash = 97 * hash + (this.schema != null ? this.schema.hashCode() : 0);
			hash = 97 * hash + (this.catalog != null ? this.catalog.hashCode() : 0);
			hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
			return hash;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final DbObjectKey other = (DbObjectKey) obj;
			if ((this.schema == null) ? (other.schema != null) : !this.schema.equals(other.schema))
				return false;
			if ((this.catalog == null) ? (other.catalog != null) : !this.catalog.equals(other.catalog))
				return false;
			if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name))
				return false;
			return true;
		}
	}
}
