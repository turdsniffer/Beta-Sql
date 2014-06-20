package com.betadb.gui.dbobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author parmstrong
 */
public class Index extends DbObject
{

	private List<Column> columns;
	private int id;//this is the id of the index on the table it has been created on

	public Index()
	{
		columns = new ArrayList<>();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setColumns(List<Column> columns)
	{
		this.columns = columns;
	}

	public List<Column> getColumns()
	{
		return columns;
	}
	
	private String getColumnsString()
	{
		StringBuilder columnString = new StringBuilder();

		for (Column column : columns)
			columnString.append(column.getName()+", ");
		if(columnString.length() > 0)
			columnString.deleteCharAt(columnString.lastIndexOf(","));		
		return columnString.toString();		
	}
	
	public Map<String,String> getProperties()
	{
		Map<String, String> properties = super.getProperties();

		if(columns.size() > 0)
			properties.put("Columns", getColumnsString());
		
		return properties;		
	}
	
	
}
