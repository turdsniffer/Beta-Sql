package com.betadb.gui.connection;

import com.betadb.gui.dbobjects.DbInfo;


/**
 * @author parmstrong
 */
public class DbConnection
{
	private DbInfo dbInfo;
	private String dataSourceKey;
	private String startingSql;

	public DbConnection(DbInfo dbInfo, String dataSourceKey)
	{
		this.dbInfo = dbInfo;
		this.dataSourceKey= dataSourceKey;
		this.startingSql = "";
	}

	public DbConnection(DbInfo dbInfo, String dataSourceKey, String startingSql)
	{
		this.dbInfo = dbInfo;
		this.dataSourceKey = dataSourceKey;
		this.startingSql = startingSql;
	}	

	public DbInfo getDbInfo()
	{
		return dbInfo;
	}

	public String getDataSourceKey()
	{
		return dataSourceKey;
	}

	public String getStartingSql()
	{
		return startingSql;
	}
	
}
