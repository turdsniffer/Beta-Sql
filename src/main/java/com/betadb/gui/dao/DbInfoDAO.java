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
import com.betadb.gui.util.Pair;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.sql.Connection;
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
		QueryRunner runner = new QueryRunner(ds);
		Connection conn = ds.getConnection();		
		
		List<String> dbName = (List)runner.query(conn, "SELECT name FROM master..sysdatabases order by name", new ColumnListHandler("name"));		
		
		List<DbInfo> retVal = new ArrayList<DbInfo>();
		for (String name : dbName)
		{
				DbInfo dbInfo = new DbInfo(name);		
				retVal.add(dbInfo);
		}
		conn.close();		
		
		return retVal;
	}
	
	public DbInfo refreshDbInfo(DbInfo info) throws SQLException
	{
		Connection conn = ds.getConnection();
		ListMultimap<Integer, Column> columns = getColumns(conn, info.getDbName());
		ListMultimap<Integer, Parameter> parameters = getParameters(conn, info.getDbName());
		ListMultimap<Integer, Index> indexes = getIndexes(conn, info.getDbName(), columns);
		info.setTables(getTables(conn, info.getDbName(), columns, indexes));		
		info.setProcedures(getProcedures(conn, info.getDbName(), parameters));
		info.setViews(getViews(conn, info.getDbName(), columns));
		info.setFunctions(getFunctions(conn, info.getDbName(), parameters));
		info.setLazyDataLoaded(true);
		return info;
	}
	
	private List<Table> getTables(Connection conn, String dbName, ListMultimap<Integer, Column> columns, ListMultimap<Integer, Index> indexMap) throws SQLException
	{	
		List<Table> tables  = new ArrayList<Table>();
		String sql = "use "+dbName+"; Select object_id as objectid, schema_name(schema_id) as schema_name, * from sys.tables where type = '"+DbObjectType.TABLE.getAbbreviation()+"' order by name";		
		QueryRunner runner = new QueryRunner(ds);
		
		List<Map<String,Object>> results = runner.query(conn, sql, new MapListHandler());		
		
		for (Map<String, Object> curRow : results)
		{
			Table table = new Table();
			table.setName(curRow.get("name").toString());
			table.setSchemaName(curRow.get("schema_name").toString());
			table.setObjectId(Integer.valueOf(curRow.get("object_id").toString()));
			for (Map.Entry<String, Object> entry : curRow.entrySet())			
				table.setProperty(entry.getKey(), entry.getValue()== null? null:entry.getValue().toString());
			table.setColumns(columns.get(table.getObjectId()));
			List<Index> indexes = indexMap.get(table.getObjectId());
			table.setIndexes(indexes);
			tables.add(table);
		}		
		
		return tables;
	}
	
	private List<Procedure> getProcedures(Connection conn, String dbName, ListMultimap<Integer, Parameter> parameterMap) throws SQLException
	{				
		String sql = "use "+dbName+"; Select object_id as objectid, name from sys.objects where type = '"+DbObjectType.PROCEDURE.getAbbreviation()+"' order by name";		
		QueryRunner runner = new QueryRunner(ds);
		List<Procedure> results = (List)runner.query(conn, sql, new BeanListHandler<Procedure>(Procedure.class));
		for (Procedure procedure : results)
			procedure.setParameters(parameterMap.get(procedure.getObjectId()));
		
		
		return results;
	}
	
	private List<View> getViews(Connection conn, String dbName, ListMultimap<Integer, Column> columns) throws SQLException
	{	
		String sql = "use "+dbName+"; Select object_id as objectid, name from sys.objects where type = '"+DbObjectType.VIEW.getAbbreviation()+"' order by name";		
		QueryRunner runner = new QueryRunner(ds);
		List<View> results = (List)runner.query(conn, sql, new BeanListHandler<View>(View.class));
		for (View view : results)
			view.setColumns(columns.get(view.getObjectId()));
		return results;
	}
	
	private List<Function> getFunctions(Connection conn, String dbName, ListMultimap<Integer, Parameter> parameterMap) throws SQLException
	{	
		String sql = "use "+dbName+"; Select object_id as objectid, name from sys.objects where type in ( '"+DbObjectType.SCALAR_FUNCTION.getAbbreviation()
				+"','"+DbObjectType.SQL_INLINE_TABLE_VALUED_FUNCTION.getAbbreviation()+
				"','"+DbObjectType.TABLE_VALUE_FUNCTION.getAbbreviation()+"') order by name";		
		QueryRunner runner = new QueryRunner(ds);
		List<Function> results = (List)runner.query(conn, sql, new BeanListHandler<Function>(Function.class));
		for (Function function : results)
			function.setParameters(parameterMap.get(function.getObjectId()));
		return results;
	}
	
	private ListMultimap<Integer,Column> getColumns(Connection conn, String dbName) throws SQLException
	{
		ListMultimap<Integer,Column> columnMap = ArrayListMultimap.create();
		String sql ="use "+dbName+"; "
				+ "Select c.*, t.name as dataType "
				+ "from sys.columns c "
				+ "join sys.types t on t.user_type_id = c.user_type_id";
		QueryRunner runner = new QueryRunner(ds);
		
		List<Map<String,Object>> results = runner.query(conn, sql, new MapListHandler());		
		
		for (Map<String, Object> curRow : results)
		{
			Column col = new Column();
			col.setName(curRow.get("name").toString());
			col.setObjectId(Integer.valueOf(curRow.get("object_id").toString()));
			for (Map.Entry<String, Object> entry : curRow.entrySet())			
				col.setProperty(entry.getKey(), entry.getValue()== null? null:entry.getValue().toString());	
			columnMap.put(col.getObjectId(), col);
		}

		return columnMap;		
	}
	
	private ListMultimap<Integer,Parameter> getParameters(Connection conn, String dbName) throws SQLException
	{
		ListMultimap<Integer,Parameter> parameterMap = ArrayListMultimap.create();
		String sql ="use "+dbName+"; "
				+ "Select p.*, t.name as dataType "
				+ "from sys.parameters p "
				+ "join sys.types t on t.user_type_id = p.user_type_id "
				+ "where p.is_output = 0";
		QueryRunner runner = new QueryRunner(ds);
		
		List<Map<String,Object>> results = runner.query(conn, sql, new MapListHandler());		
		
		for (Map<String, Object> curRow : results)
		{
			Parameter parameter = new Parameter();
			parameter.setName(curRow.get("name").toString());
			parameter.setObjectId(Integer.valueOf(curRow.get("object_id").toString()));
			for (Map.Entry<String, Object> entry : curRow.entrySet())			
				parameter.setProperty(entry.getKey(), entry.getValue()== null? null:entry.getValue().toString());	
			parameterMap.put(parameter.getObjectId(), parameter);
		}

		return parameterMap;		
	}
	
	public String getScript(DbObject dbObject, String dbName) throws SQLException
	{
		
		
		String sql = "use "+dbName+"; exec sp_helpText "+dbObject.getName();		
		QueryRunner runner = new QueryRunner(ds);
		List<String> results = (List)runner.query(sql, new ColumnListHandler());
		return StringUtils.join(results, "");
	}
	
	private ListMultimap<Integer,Index> getIndexes(Connection conn, String dbName, ListMultimap<Integer, Column> columns) throws SQLException
	{
		ListMultimap<Integer,Index> retVal = ArrayListMultimap.create();
		ListMultimap<Pair<Integer, Integer>, IndexColumn> indexColumnMap = getIndexColumns(conn, dbName);
		
		String sql = "use "+dbName+"; "
				+ "select i.* from sys.indexes i";

		QueryRunner runner = new QueryRunner(ds);
		
		List<Map<String,Object>> results = runner.query(conn, sql, new MapListHandler());	
		
		for (Map<String, Object> curRow : results)
		{
			Index index = new Index();
			Object name = curRow.get("name");
			if(name != null)
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
					if(column.getId() == indexColumn.getColumnId())
					{
						cols.add(column);
						break;
					}
				}
			}
			index.setColumns(cols);
			//----------------------------------------------
			
			
			for (Map.Entry<String, Object> entry : curRow.entrySet())			
				index.setProperty(entry.getKey(), entry.getValue()== null? null:entry.getValue().toString());		
			
			retVal.put(index.getObjectId(), index);
		}

		return retVal;
	}
	
	private ListMultimap<Pair<Integer,Integer>,IndexColumn> getIndexColumns(Connection conn, String dbName)  throws SQLException
	{
		ListMultimap<Pair<Integer,Integer>,IndexColumn> retVal = ArrayListMultimap.create();
		
		String sql = "use "+dbName+"; "
				+ "select object_id as objectId, index_id as indexId, column_id as columnId, index_column_id as indexColumnId from sys.index_columns order by object_id, index_column_id";
		QueryRunner runner = new QueryRunner(ds);
		
		List<IndexColumn> results = runner.query(conn, sql, new BeanListHandler<IndexColumn>(IndexColumn.class));
		for (IndexColumn indexColumn : results)
			retVal.put(new Pair<Integer, Integer>(indexColumn.getObjectId(), indexColumn.getIndexId()), indexColumn);
		
		return retVal;
	}			
}
