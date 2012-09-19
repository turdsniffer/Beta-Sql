package com.betadb.gui.dbobjects;

/**
 * @author parmstrong
 */
public class Column extends DbObject
{
	public String getDataType()
	{
		return getProperty("dataType").toString();
	}

	public String getLength()
	{
		return getProperty("precision").toString();
	}
	
	public Integer getId()
	{
		return Integer.valueOf(getProperty("column_id").toString());
	}
}
