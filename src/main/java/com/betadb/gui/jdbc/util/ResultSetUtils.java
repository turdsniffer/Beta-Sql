
package com.betadb.gui.jdbc.util;

import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author parmstrong
 */
public class ResultSetUtils
{
	public static String[] getColumnNames(ResultSet rs) throws SQLException
	{
		ResultSetMetaData metaData = rs.getMetaData();

		int numColumns = metaData.getColumnCount();
		String[] columns = new String[numColumns];

		for (int i = 1; i < numColumns + 1; i++)
			columns[i - 1] = metaData.getColumnName(i);

		return columns;
	}

	public static Class[] getColumnClasses(ResultSet rs) throws SQLException
	{
		ResultSetMetaData metaData = rs.getMetaData();

		int numColumns = metaData.getColumnCount();
		Class[] classes = new Class[numColumns];
		for (int i = 1; i < numColumns + 1; i++)
		{
			try
			{
				classes[i - 1] = Class.forName(metaData.getColumnClassName(i));
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
		Map<String,String> properties = Maps.newHashMap();
		String[] columnNames = getColumnNames(rs);

		for (int i = 1; i <= columnNames.length; i++)
			properties.put(columnNames[i-1], String.valueOf(rs.getObject(i))) ;

		return properties;
	}

}
