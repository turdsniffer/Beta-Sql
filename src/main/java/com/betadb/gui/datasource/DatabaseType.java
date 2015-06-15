/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.betadb.gui.datasource;

/**
 *
 * @author parmstrong
 */
public enum DatabaseType
{
	MSSQL("MS Sql Server", "net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://"),
	POSTGRES("Postgres", "org.postgresql.Driver", "jdbc:postgresql://"),
	SYBASE("Sybase","net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sybase://");

	private String displayName;
	private String driverClassName;
	private String url;

	private DatabaseType(String displayName, String driverClassName, String url)
	{
		this.displayName = displayName;
		this.driverClassName = driverClassName;
		this.url = url;
	}

	@Override
	public String toString()
	{
		return getDisplayName();
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getDriverClassName()
	{
		return driverClassName;
	}

	public String getUrl()
	{
		return url;
	}
}
