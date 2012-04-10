package com.clearwateranalytics.betadb.gui.connection;

import com.clearwateranalytics.betadb.gui.dbobjects.DbInfo;


/**
 * @author parmstrong
 */
public class ConnectionInfo
{
	private DbInfo dbInfo;
	private String dataSourceKey;
	private String startingSql;

	public ConnectionInfo(DbInfo dbInfo, String dataSourceKey)
	{
		this.dbInfo = dbInfo;
		this.dataSourceKey= dataSourceKey;
		this.startingSql = "";
	}

	public ConnectionInfo(DbInfo dbInfo, String dataSourceKey, String startingSql)
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
