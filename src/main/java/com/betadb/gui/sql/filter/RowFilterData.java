/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.betadb.gui.sql.filter;

/**
 *
 * @author parmstrong
 */
public class RowFilterData
{
	final private int column;
	final private javax.swing.RowFilter.ComparisonType comparisonType;
	final private Comparable value;

	public RowFilterData(int column, javax.swing.RowFilter.ComparisonType comparisonType, Comparable value)
	{
		this.column = column;
		this.comparisonType = comparisonType;
		this.value = value;
	}	
	
	/**
	 * @return the value
	 */
	public Comparable getValue()
	{
		return value;
	}

	/**
	 * @return the column
	 */
	public int getColumn()
	{
		return column;
	}
	


	/**
	 * @return the comparisonType
	 */
	public javax.swing.RowFilter.ComparisonType getComparisonType()
	{
		return comparisonType;
	}
}
