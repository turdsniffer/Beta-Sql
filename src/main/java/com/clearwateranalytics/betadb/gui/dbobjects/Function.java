package com.clearwateranalytics.betadb.gui.dbobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author parmstrong
 */
public class Function extends DbObject
{
	private List<Parameter> parameters;

	public Function()
	{
		this.parameters = new ArrayList<Parameter>();
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
		String retVal = this.getName();
		retVal+="(";
		for (Parameter parameter : getParameters())
		{
			retVal+=parameter.getName()+", ";
		}
		if(getParameters().size() > 0)
			retVal = retVal.substring(0, retVal.length()-2);
		retVal+=")";
		
		return retVal;
	}	
}
