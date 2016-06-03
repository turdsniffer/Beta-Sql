/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.betadb.gui.connection;

import com.betadb.gui.datasource.DatabaseType;
import java.util.Objects;

/**
 *
 * @author Phil
 */
public class ConnectionInfo
{
	private final String serverName;
	private final String userName;
	private final String password;
	private final String domain;
	private final String instanceName;
	private final String port;
	private final DatabaseType databaseType;
	

	public ConnectionInfo(String serverName, String userName, String password, String domain, String instanceName, String port, DatabaseType databaseType)
	{
		this.serverName = serverName;
		this.userName = userName;
		this.domain = domain;
		this.instanceName = instanceName;
		this.port = port;
		this.password = password;
		this.databaseType = databaseType;
	}

	public String getServerName()
	{
		return serverName;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getDomain()
	{
		return domain;
	}

	public String getInstanceName()
	{
		return instanceName;
	}
	
	public String getPort()
	{
		return port;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 41 * hash + Objects.hashCode(this.serverName);
		hash = 41 * hash + Objects.hashCode(this.userName);
		hash = 41 * hash + Objects.hashCode(this.instanceName);
		hash = 41 * hash + Objects.hashCode(this.databaseType);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ConnectionInfo other = (ConnectionInfo) obj;
		if (!Objects.equals(this.serverName, other.serverName))
			return false;
		if (!Objects.equals(this.userName, other.userName))
			return false;
		if (!Objects.equals(this.instanceName, other.instanceName))
			return false;
		if (this.databaseType != other.databaseType)
			return false;
		return true;
	}



	@Override
	public String toString()
	{
		String instanceNameSuffix = instanceName == null || instanceName.isEmpty() ? "" : "\\"+instanceName;
		return serverName + instanceNameSuffix;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @return the databaseType
	 */
	public DatabaseType getDatabaseType()
	{
		return databaseType;
	}

}
