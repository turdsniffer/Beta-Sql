package com.betadb.gui.connection;

import com.betadb.gui.datasource.DataSourceKey;
import com.betadb.gui.dbobjects.DbInfo;


/**
 * @author parmstrong
 */
public class DbConnection
{
	private final DbInfo dbInfo;
	private final DataSourceKey dataSourceKey;
	private final String startingSql;

	public DbConnection(DbInfo dbInfo, DataSourceKey dataSourceKey)
	{
		this.dbInfo = dbInfo;
		this.dataSourceKey= dataSourceKey;
		this.startingSql = "";
	}

	public DbConnection(DbInfo dbInfo, DataSourceKey dataSourceKey, String startingSql)
	{
		this.dbInfo = dbInfo;
		this.dataSourceKey = dataSourceKey;
		this.startingSql = startingSql;
	}	

	public DbInfo getDbInfo()
	{
		return dbInfo;
	}

	public DataSourceKey getDataSourceKey()
	{
		return dataSourceKey;
	}

	public String getStartingSql()
	{
		return startingSql;
	}
	
}
