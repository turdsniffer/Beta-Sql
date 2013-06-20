package com.betadb.gui.dbobjects;


import com.swingautocompletion.main.AutoCompleteItem;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author parmstrong
 */
public class DbObject extends AutoCompleteItem
{
	private String name;
	private int objectId;
	private String schemaName;
	private Map<String,String> properties = new TreeMap<String, String>();

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
		return getName();
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
}
