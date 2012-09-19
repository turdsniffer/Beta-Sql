package com.betadb.gui.dbobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author parmstrong
 */
public class DbInfo
{
	private String dbName;
	
	private boolean lazyDataLoaded;
	private List<Table>tables;
	private List<Procedure>procedures;
	private List<View>views;
	private List<Function>functions;

	public DbInfo(String dbName)
	{
		this.dbName = dbName;
		lazyDataLoaded = false;
		tables = new ArrayList<Table>();
		procedures = new ArrayList<Procedure>();
		views = new ArrayList<View>();
		functions = new ArrayList<Function>();
	}	
	
	public String getDbName()
	{
		return dbName;
	}
	
	@Override
	public String toString()
	{
		return dbName;
	}

	public List<Table> getTables()
	{
		return tables;
	}

	public void setTables(List<Table> tables)
	{
		this.tables = tables;
	}

	public List<Procedure> getProcedures()
	{
		return procedures;
	}

	public void setProcedures(List<Procedure> procedures)
	{
		this.procedures = procedures;
	}

	public List<View> getViews()
	{
		return views;
	}

	public void setViews(List<View> views)
	{
		this.views = views;
	}

	public boolean isLazyDataLoaded()
	{
		return lazyDataLoaded;
	}

	public void setLazyDataLoaded(boolean lazyDataLoaded)
	{
		this.lazyDataLoaded = lazyDataLoaded;
	}

	public List<Function> getFunctions()
	{
		return functions;
	}

	public void setFunctions(List<Function> functions)
	{
		this.functions = functions;
	}
	
}
