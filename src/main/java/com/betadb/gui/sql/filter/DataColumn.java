package com.betadb.gui.sql.filter;

/**
 *
 * @author parmstrong
 */
public class DataColumn
{
	private int index;
	private String name;

	public DataColumn(int index, String name)
	{
		this.index = index;
		this.name = name;
	}
	
	/**
	 * @return the index
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name;
	}
	
	
}
