package com.betadb.gui.dbobjects;

/**
 * @author parmstrong
 */
public class Column extends DbObject
{
	private String dataType;
	private int decimalDigits;

	public String getDataType()
	{
		return dataType;
	}

	public int getDecimalDigits()
	{
		return decimalDigits;
	}
	
	public Integer getId()
	{
		return Integer.valueOf(getProperty("column_id").toString());
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public void setDecimalDigits(int decimalDigits)
	{
		this.decimalDigits = decimalDigits;
	}
}
