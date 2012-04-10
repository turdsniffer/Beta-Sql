/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clearwateranalytics.betadb.gui.dbobjects;

/**
 *
 * @author parmstrong
 */
public enum DbObjectType
{
	TABLE("U"),
	PROCEDURE("P"),
	VIEW("V"),
	TABLE_VALUE_FUNCTION("TF"),
	SCALAR_FUNCTION("FN"),
	SQL_INLINE_TABLE_VALUED_FUNCTION("IF"),
	OTHER("");
	
	private String abbreviation;

	private DbObjectType(String abbreviation)
	{
		this.abbreviation = abbreviation;
	}
	
	public static DbObjectType getByAbbreviation(String abbreviation)
	{
		for (DbObjectType type : DbObjectType.values())
		{
			if(type.getAbbreviation().equalsIgnoreCase(abbreviation))
				return type;
		}
		return OTHER;
	}

	public String getAbbreviation()
	{
		return abbreviation;
	}
	
}
