package com.betadb.gui.dbobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author parmstrong
 */
public class Procedure extends DbObject
{
	private List<Parameter> parameters;

	public Procedure()
	{
		this.parameters = new ArrayList<>();
	}	
	
	public void setParameters(List<Parameter> parameters)
	{
		this.parameters = parameters;
	}

	public List<Parameter> getParameters()
	{
		return parameters;
	}


		
	@Override
	public String getAutoCompletion()
	{
		String retVal = getSchemaName()+"."+getName();
		for (Parameter parameter : getParameters())
		{
			retVal+=" "+parameter.getName()+" = ?,";
		}
		if(getParameters().size() > 0)
			retVal = retVal.substring(0, retVal.length()-1);
		
		return retVal;
	}	
}
