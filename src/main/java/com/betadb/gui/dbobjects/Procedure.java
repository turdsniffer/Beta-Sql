package com.betadb.gui.dbobjects;

import com.betadb.gui.dao.DbInfoDAO;
import java.util.List;

/**
 * @author parmstrong
 */
public class Procedure extends DbObject
{
	private List<Parameter> parameters;
    private DbInfoDAO dbInfoDAO;
    
	public Procedure(DbInfoDAO dbInfoDAO)
	{	
        this.dbInfoDAO = dbInfoDAO;
	}	
	


	public List<Parameter> getParameters()
	{
        if(parameters == null)
            dbInfoDAO.getProcedureParameters(this);
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
