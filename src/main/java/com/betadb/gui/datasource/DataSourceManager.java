package com.betadb.gui.datasource;

import com.betadb.gui.events.Event;
import com.betadb.gui.events.EventManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

@Singleton
public class DataSourceManager {

	private static final Logger logger = getLogger(DataSourceManager.class.getName());

	private final Map<DataSourceKey,DataSource>  datasourceMap = new HashMap<>();
	@Inject private EventManager eventManager;
	
	public Map<DataSourceKey,DataSource> getDataSources()
	{
		return datasourceMap;
	}
	
	public DataSource getDataSource(DataSourceKey key,String port, DatabaseType databaseType, String username, String password,  String domain)
	{
		DataSource ds = datasourceMap.get(key);
		if(ds==null)
		{
			try
			{
				ds = initDataAccess(key, port, databaseType, username, password, domain);
				addDataSource(key, ds);
			}
			catch (SQLException ex)
			{
				logger.log(Level.SEVERE, "Failed to instantiate data access. " + ex.toString() );
				throw new RuntimeException();
			}
		}
		return ds;
	}

	public void addDataSource(DataSourceKey key, DataSource ds)
	{
		datasourceMap.put(key, ds);
		eventManager.fireEvent(Event.DATA_SOURCE_ADDED, key);
	}
	
	public DataSource getDataSourceByDbId(DataSourceKey key)
	{
		return datasourceMap.get(key);
	}

	public String getDataSourceKey(String dbServerName, String instanceName, String dbName)
	{
		String key = dbServerName;
		if(dbName != null && dbName.length() > 0)
			key+="/"+dbName;
		if(instanceName != null && instanceName.length() > 0)
			key+="/"+instanceName;
		
		
		return key;
	}
	

	private DataSource initDataAccess(DataSourceKey key, String port, DatabaseType databaseType, String username, String password, String domain) throws SQLException
	{
		BasicDataSource dataSource = null;
		Connection connection = null;
		try
		{
			dataSource = new BasicDataSource()
			{
				public String toString()
				{
					return this.getUrl();
				}
			};
			dataSource.setMaxActive( 100 );
			dataSource.setMaxIdle( 30 );
			dataSource.setDriverClassName( databaseType.getDriverClassName() );
			String connectUrl = databaseType.getUrl()+key.getDbServerName();
			if(port != null)
				connectUrl+=":"+port;
			if(key.getDbName()!= null && key.getDbName().length()>0)
				connectUrl+="/"+key.getDbName();
			if(domain != null && domain.length()>0)
				connectUrl+=";domain="+domain;		
			if(key.getInstanceName() != null && key.getInstanceName().length()>0)
				connectUrl+=";instance="+key.getInstanceName();
			
			dataSource.setUrl(connectUrl);
			dataSource.setUsername( username );
			dataSource.setPassword( password );
			dataSource.setValidationQuery( "select 1" );
			connection = dataSource.getConnection();			
		}
		finally
		{		
			if(connection != null)
				connection.close();
		}
		return dataSource;
	}
}
