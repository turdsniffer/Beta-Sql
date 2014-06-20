package com.betadb.gui.dbobjects;

import static java.lang.Integer.valueOf;

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
		return valueOf(getProperty("column_id").toString());
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public void setDecimalDigits(int decimalDigits)
	{
		this.decimalDigits = decimalDigits;
	}

	@Override
	public String getAutoCompleteId()
	{
		return getName();
	}

	@Override
	public String getDescription()
	{
		return dataType;
	}
}
