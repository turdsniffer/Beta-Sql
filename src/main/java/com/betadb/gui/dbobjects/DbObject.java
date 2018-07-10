package com.betadb.gui.dbobjects;


import com.google.common.collect.Lists;
import com.swingautocompletion.main.AutoCompleteItem;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author parmstrong
 */
public class DbObject extends AutoCompleteItem
{
	protected String name;
	private int objectId;
	private String schemaName;
    private String databaseName;
	private Map<String,String> properties = new TreeMap<>();
	private DbObjectType objectType;
	
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	public int getObjectId()
	{
		return objectId;
	}

	public void setObjectId(int objectId)
	{
		this.objectId = objectId;
	}

	@Override
	public String getAutoCompleteId()
	{      
        String dbName = getDatabaseName() != null ?getDatabaseName() +".": "";        
        String schemaName = getSchemaName()!= null ? getSchemaName() +".": "";   
        
		return dbName + schemaName + getName();
	}

	@Override
	public List<String> alternateAutoCompeteIds()
	{
        List<String> retVal = Lists.newArrayList();
        if(schemaName != null)
			retVal.add(getSchemaName()+"."+getName());
		retVal.add(getName());
		return retVal;
	}
	
	public String getAutoCompletion()
	{        
        String dbName = getDatabaseName() != null ?getDatabaseName() +".": "";        
        String schemaName = getSchemaName()!= null ? getSchemaName() +".": "";   
        
		String autoCompletion = dbName+schemaName+this.getName();
		return autoCompletion.contains(" ") ? "["+autoCompletion+"]" : autoCompletion;
	}
	

	public String getSchemaName()
	{
		return schemaName;
	}

	public void setSchemaName(String schemaName)
	{
		this.schemaName = schemaName;
	}

	public Map<String,String> getProperties()
	{
		return properties;
	}

	public void setProperties(Map<String,String> properties)
	{
		this.properties = properties;
	}
	
	public void setProperty(String name, String value)
	{
		this.properties.put(name, value);
	}
	
	public Object getProperty(String name)
	{
		return properties.get(name);
	}

	/**
	 * @return the objectType
	 */
	public DbObjectType getObjectType()
	{
		return objectType;
	}

	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(DbObjectType objectType)
	{
		this.objectType = objectType;
	}
    
    
    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }
    
}
