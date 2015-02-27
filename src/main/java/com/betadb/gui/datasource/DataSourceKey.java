
package com.betadb.gui.datasource;

/**
 * @author parmstrong
 */
public class DataSourceKey
{
	private final String serverName;
	private final String instanceName;
	private final String dbName;

	public DataSourceKey(String dbServerName, String instanceName, String dbName)
	{
		this.serverName = dbServerName;
		this.instanceName = instanceName;
		this.dbName = dbName;
	}

	public String getDbServerName()
	{
		return serverName;
	}

	public String getInstanceName()
	{
		return instanceName;
	}

	public String getDbName()
	{
		return dbName;
	}

	@Override
	public String toString()
	{
		String key = serverName;
		if(dbName != null && dbName.length() > 0)
			key+="/"+dbName;
		if(instanceName != null && instanceName.length() > 0)
			key+="/"+instanceName;
		return key;
	}
}
