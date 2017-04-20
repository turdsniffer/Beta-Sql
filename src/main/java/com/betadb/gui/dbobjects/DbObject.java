package com.betadb.gui.dbobjects;


import com.swingautocompletion.main.AutoCompleteItem;
import java.util.Collections;
import static java.util.Collections.singletonList;
import java.util.List;
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
		return getSchemaName()+"."+getName();
	}

	@Override
	public List<String> alternateAutoCompeteIds()
	{
		if(schemaName.equals("dbo"))
			return singletonList(getName());
		return Collections.EMPTY_LIST;
	}
	
	public String getAutoCompletion()
	{
		String autoCompletion = super.getAutoCompletion();
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
}
