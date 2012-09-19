/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.betadb.gui.datasource;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class SQLUtils
{
	public static void close(Connection conn, Statement stmt, ResultSet rs)
	{
		close(rs);
		close(stmt);
		close(conn);
	}

	public static void close(Connection conn, Statement stmt)
	{
		close(stmt);
		close(conn);
	}

	public static void close(Connection conn)
	{
		try
		{
			if (conn != null)
				conn.close();
		}
		catch (SQLException ex)
		{
			throw new RuntimeException("Unable to close SQL connection.", ex);
		}
	}

	public static void close(ResultSet rs)
	{
		try
		{
			if (rs != null)
				rs.close();
		}
		catch (SQLException ex)
		{
			throw new RuntimeException("Unable to close SQL result set.", ex);
		}
	}

	public static void close(Statement stmt)
	{
		try
		{
			if (stmt != null)
				stmt.close();
		}
		catch (SQLException ex)
		{
			throw new RuntimeException("Unable to close SQL statement.", ex);
		}
	}
}