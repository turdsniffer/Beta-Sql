
package com.betadb.gui.datasource;

import java.util.Objects;

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

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.serverName);
        hash = 97 * hash + Objects.hashCode(this.instanceName);
        hash = 97 * hash + Objects.hashCode(this.dbName);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DataSourceKey other = (DataSourceKey) obj;
        if (!Objects.equals(this.serverName, other.serverName))
            return false;
        if (!Objects.equals(this.instanceName, other.instanceName))
            return false;
        if (!Objects.equals(this.dbName, other.dbName))
            return false;
        return true;
    }
    
    
}
