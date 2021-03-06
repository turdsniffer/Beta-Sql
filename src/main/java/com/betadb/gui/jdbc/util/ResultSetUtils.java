
package com.betadb.gui.jdbc.util;

import com.google.common.collect.Maps;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.Class.forName;
import static java.lang.String.valueOf;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author parmstrong
 */
public class ResultSetUtils
{
	public static List<String> getColumnNames(ResultSet rs) throws SQLException
	{
		ResultSetMetaData metaData = rs.getMetaData();

		int numColumns = metaData.getColumnCount();
		List<String> columns = new ArrayList<>(numColumns);

		for (int i = 1; i < numColumns + 1; i++)
			columns.add(i - 1, metaData.getColumnName(i));

		return columns;
	}

	public static List<Class> getColumnClasses(ResultSet rs) throws SQLException
	{
		ResultSetMetaData metaData = rs.getMetaData();

		int numColumns = metaData.getColumnCount();

		List<Class> classes = new ArrayList<>();
		for (int i = 1; i < numColumns + 1; i++)
		{
			try
			{
				classes.add(i - 1, forName(metaData.getColumnClassName(i)));
			}
			catch (ClassNotFoundException ex)
			{
				continue;
			}
		}
		return classes;
	}

	/**
	 * converts the current result set row into a map of column name and value pairs 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static Map<String,String> getRowAsProperties(ResultSet rs) throws SQLException
	{
		Map<String,String> properties = newHashMap();
		List<String> columnNames = getColumnNames(rs);

		for (int i = 1; i <= columnNames.size(); i++)
			properties.put(columnNames.get(i-1), valueOf(rs.getObject(i))) ;

		return properties;
	}

}
