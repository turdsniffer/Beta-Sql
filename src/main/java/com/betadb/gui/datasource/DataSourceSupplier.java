package com.betadb.gui.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;


public class DataSourceSupplier {

	private static final Logger logger = Logger.getLogger(DataSourceSupplier.class.getName());
	
	private static DataSourceSupplier supplier;

	private Map<String,DataSource>  datasourceMap = new HashMap<String, DataSource>();

	private DataSourceSupplier()
	{}	
	
	public static DataSourceSupplier getInstance()
	{		
		if(supplier == null)
			supplier = new DataSourceSupplier();
		return supplier;
	}	
	
	public Map<String,DataSource> getDataSources()
	{
		return datasourceMap;
	}
	
	public DataSource getDataSource(DatabaseType databaseType, String dbServerName, String username, String password, String dbName, String domain, String instanceName)
	{
		DataSource ds = datasourceMap.get(getDataSourceKey(dbServerName, instanceName,""));
		if(ds==null)
		{
			try
			{
				ds = initDataAccess(databaseType, dbServerName, username, password, dbName, domain, instanceName);
				datasourceMap.put(getDataSourceKey(dbServerName, instanceName, dbName), ds);
			}
			catch (SQLException ex)
			{
				logger.log(Level.SEVERE, "Failed to instantiate data access. " + ex.toString() );
				throw new RuntimeException();
			}
		}
		return ds;
	}
	
	public DataSource getDataSourceByDbId(String dbIdentifier)
	{
		return datasourceMap.get(dbIdentifier);
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
	

	private DataSource initDataAccess(DatabaseType databaseType, String dbServerName, String username, String password, String dbName, String domain, String instanceName) throws SQLException
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
			String connectUrl = databaseType.getUrl()+dbServerName;
			if(dbName!= null && dbName.length()>0)
				connectUrl+="/"+dbName;				
			if(domain != null && domain.length()>0)
				connectUrl+=";domain="+domain;		
			if(instanceName != null && instanceName.length()>0)
				connectUrl+=";instance="+instanceName;
			dataSource.setUrl(connectUrl);
			dataSource.setUsername( username  );
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
