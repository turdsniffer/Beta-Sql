package com.betadb.gui.dao;

import com.betadb.gui.dbobjects.Column;
import com.betadb.gui.dbobjects.DbInfo;
import com.betadb.gui.dbobjects.DbObject;
import com.betadb.gui.dbobjects.DbObjectType;
import com.betadb.gui.dbobjects.Function;
import com.betadb.gui.dbobjects.Index;
import com.betadb.gui.dbobjects.IndexColumn;
import com.betadb.gui.dbobjects.Parameter;
import com.betadb.gui.dbobjects.Procedure;
import com.betadb.gui.dbobjects.Table;
import com.betadb.gui.dbobjects.View;
import com.betadb.gui.jdbc.util.ResultSetUtils;
import com.betadb.gui.util.Pair;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * @author parmstrong
 */
public class DbInfoDAO
{
	DataSource ds;

	//add query from sys.system_objects, sys.system_columns for system views and stored procs
	public DbInfoDAO(DataSource ds)
	{
		this.ds = ds;
	}

	public List<DbInfo> getDatabases() throws SQLException
	{
		Connection conn = ds.getConnection();
		ResultSet rs = conn.getMetaData().getCatalogs();
		List<DbInfo> retVal = new ArrayList<DbInfo>();
		while (rs.next())
		{
			DbInfo dbInfo = new DbInfo(rs.getString(1));
			retVal.add(dbInfo);
		}
		rs.close();
		conn.close();

		return retVal;
	}

	public DbInfo refreshDbInfo(DbInfo info) throws SQLException
	{
		Connection conn = ds.getConnection();
		DatabaseMetaData metaData = conn.getMetaData();
		ListMultimap<DbObjectKey, Column> columns = getColumns(metaData, info.getDbName());
		ListMultimap<DbObjectKey, Parameter> parameters = getParameters(metaData, info.getDbName());
		ListMultimap<Integer, Index> indexes = ArrayListMultimap.create();//getIndexes(conn, info.getDbName(), get);
		Pair<List<Table>, List<View>> tablesAndViewsPair = getTablesAndViews(metaData, info.getDbName(), columns, indexes);
		info.setTables(tablesAndViewsPair.getFirst());
		info.setProcedures(getProcedures(metaData, info.getDbName(), parameters));
		info.setViews(tablesAndViewsPair.getSecond());
		info.setFunctions(getFunctions(conn, info.getDbName(), parameters));
		info.setLazyDataLoaded(true);
		return info;
	}

	private Pair<List<Table>, List<View>> getTablesAndViews(DatabaseMetaData databaseMetaData, String dbName, ListMultimap<DbObjectKey, Column> columns, ListMultimap<Integer, Index> indexMap) throws SQLException
	{
		List<Table> tables = new ArrayList<Table>();
		List<View> views = new ArrayList<View>();

		ResultSet rs = databaseMetaData.getTables(dbName, null, null, null);

		while (rs.next())
		{
			Table table = new Table();
			if ("VIEW".equals(rs.getString("TABLE_TYPE")))
			{
				View view = new View();
				views.add(view);
				table = view;
			}
			else
				tables.add(table);
			table.setSchemaName(rs.getString("TABLE_SCHEM"));
			table.setName(rs.getString("TABLE_NAME"));
			table.setProperties(ResultSetUtils.getRowAsProperties(rs));
			table.setColumns(columns.get(new DbObjectKey(dbName, table.getSchemaName(), table.getName())));
		}

		return new Pair<List<Table>, List<View>>(tables, views);
	}



	private ListMultimap<DbObjectKey, Column> getColumns(DatabaseMetaData databaseMetaData, String catalog) throws SQLException
	{
		ListMultimap<DbObjectKey, Column> retVal = ArrayListMultimap.create();

		ResultSet rs = databaseMetaData.getColumns(catalog, null, null, null);
		while (rs.next())
		{
			Column c = new Column();
			c.setName(rs.getString("COLUMN_NAME"));
			c.setSchemaName(rs.getString("TABLE_SCHEM"));
			c.setDecimalDigits(rs.getInt("COLUMN_SIZE"));
			c.setDataType(rs.getString("TYPE_NAME"));
			c.setProperties(ResultSetUtils.getRowAsProperties(rs));
			retVal.put(new DbObjectKey(catalog, rs.getString("TABLE_SCHEM"), rs.getString("TABLE_NAME")), c);
		}
		return retVal;
	}

	private List<Procedure> getProcedures(DatabaseMetaData databaseMetaData, String dbName, ListMultimap<DbObjectKey, Parameter> parameterMap) throws SQLException
	{
		List<Procedure> results = new ArrayList<Procedure>();

		ResultSet rs = databaseMetaData.getProcedures(dbName, null, null);
		while(rs.next())
		{
			Procedure procedure = new Procedure();
			procedure.setName(rs.getString("PROCEDURE_NAME"));
			procedure.setSchemaName(rs.getString("PROCEDURE_SCHEM"));
			procedure.setProperties(ResultSetUtils.getRowAsProperties(rs));
			procedure.setParameters(parameterMap.get(new DbObjectKey(dbName, procedure.getSchemaName(), procedure.getName())));
			results.add(procedure);
		}

		return results;
	}

	private List<Function> getFunctions(Connection conn, String dbName, ListMultimap<DbObjectKey, Parameter> parameterMap) throws SQLException
	{
		String sql = "use " + dbName + "; Select object_id as objectid, schema_name(schema_id) as schemaName, name from sys.objects where type in ( '" + DbObjectType.SCALAR_FUNCTION.getAbbreviation()
					 + "','" + DbObjectType.SQL_INLINE_TABLE_VALUED_FUNCTION.getAbbreviation()
					 + "','" + DbObjectType.TABLE_VALUE_FUNCTION.getAbbreviation() + "') order by name";
		QueryRunner runner = new QueryRunner(ds);
		List<Function> results = (List) runner.query(conn, sql, new BeanListHandler<Function>(Function.class));
//		for (Function function : results)
//			function.setParameters(parameterMap.get(function.getObjectId()));
		return results;
	}

	private ListMultimap<DbObjectKey, Parameter> getParameters(DatabaseMetaData metaData, String dbName) throws SQLException
	{
		ResultSet rs = metaData.getProcedureColumns(dbName, null, null, null);
		ListMultimap<DbObjectKey, Parameter> parameterMap = ArrayListMultimap.create();

		while(rs.next())
		{
			Parameter parameter = new Parameter();
			parameter.setName(rs.getString("COLUMN_NAME"));
			parameter.setSchemaName(rs.getString("PROCEDURE_SCHEM"));
			parameter.setProperties(ResultSetUtils.getRowAsProperties(rs));
			parameterMap.put(new DbObjectKey(dbName, parameter.getSchemaName(), rs.getString("PROCEDURE_NAME")), parameter);
		}
		return parameterMap;
	}

	public String getScript(DbObject dbObject, String dbName) throws SQLException
	{


		String sql = "use " + dbName + "; exec sp_helpText " + dbObject.getName();
		QueryRunner runner = new QueryRunner(ds);
		List<String> results = (List) runner.query(sql, new ColumnListHandler());
		return StringUtils.join(results, "");
	}

	private ListMultimap<Integer, Index> getIndexes(Connection conn, String dbName, ListMultimap<Integer, Column> columns) throws SQLException
	{
		ListMultimap<Integer, Index> retVal = ArrayListMultimap.create();
		ListMultimap<Pair<Integer, Integer>, IndexColumn> indexColumnMap = getIndexColumns(conn, dbName);

		String sql = "use " + dbName + "; "
					 + "select i.* from sys.indexes i";

		QueryRunner runner = new QueryRunner(ds);

		List<Map<String, Object>> results = runner.query(conn, sql, new MapListHandler());

		for (Map<String, Object> curRow : results)
		{
			Index index = new Index();
			Object name = curRow.get("name");
			if (name != null)
				index.setName(name.toString());
			index.setId(Integer.valueOf(curRow.get("index_id").toString()));
			index.setObjectId(Integer.valueOf(curRow.get("object_id").toString()));

			//Get the columns included in the index.
			List<IndexColumn> indexColumns = indexColumnMap.get(new Pair<Integer, Integer>(index.getObjectId(), index.getId()));
			List<Column> tableColumns = columns.get(index.getObjectId());
			List<Column> cols = new ArrayList<Column>();
			for (IndexColumn indexColumn : indexColumns)
			{
				for (Column column : tableColumns)
				{
					if (column.getId() == indexColumn.getColumnId())
					{
						cols.add(column);
						break;
					}
				}
			}
			index.setColumns(cols);
			//----------------------------------------------


			for (Map.Entry<String, Object> entry : curRow.entrySet())
				index.setProperty(entry.getKey(), entry.getValue() == null ? null : entry.getValue().toString());

			retVal.put(index.getObjectId(), index);
		}

		return retVal;
	}

	private ListMultimap<Pair<Integer, Integer>, IndexColumn> getIndexColumns(Connection conn, String dbName) throws SQLException
	{
		ListMultimap<Pair<Integer, Integer>, IndexColumn> retVal = ArrayListMultimap.create();

		String sql = "use " + dbName + "; "
					 + "select object_id as objectId, index_id as indexId, column_id as columnId, index_column_id as indexColumnId from sys.index_columns order by object_id, index_column_id";
		QueryRunner runner = new QueryRunner(ds);

		List<IndexColumn> results = runner.query(conn, sql, new BeanListHandler<IndexColumn>(IndexColumn.class));
		for (IndexColumn indexColumn : results)
			retVal.put(new Pair<Integer, Integer>(indexColumn.getObjectId(), indexColumn.getIndexId()), indexColumn);

		return retVal;
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
