/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.betadb.gui.connection;

/**
 *
 * @author Phil
 */
public class ConnectionInfo
{

	private String serverName;
	private String userName;
	private String domain;
	private String instanceName;
	private String port;

	public ConnectionInfo(String serverName, String userName, String domain, String instanceName, String port)
	{
		this.serverName = serverName;
		this.userName = userName;
		this.domain = domain;
		this.instanceName = instanceName;
		this.port = port;
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
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ConnectionInfo other = (ConnectionInfo) obj;
		if ((this.serverName == null) ? (other.serverName != null) : !this.serverName.equals(other.serverName))
			return false;
		if ((this.domain == null) ? (other.domain != null) : !this.domain.equals(other.domain))
			return false;
		if ((this.instanceName == null) ? (other.instanceName != null) : !this.instanceName.equals(other.instanceName))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		return hash;
	}

	@Override
	public String toString()
	{
		String instanceNameSuffix = instanceName == null || instanceName.isEmpty() ? "" : "\\"+instanceName;
		return serverName + instanceNameSuffix;
	}

}
