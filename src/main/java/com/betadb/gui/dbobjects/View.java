package com.betadb.gui.dbobjects;



import com.swingautocompletion.main.AutoCompleteItem;
import java.util.ArrayList;
import java.util.List;

/**
 * @author parmstrong
 */
public class View extends DbObject
{
	private List<Column> columns;
	
	public List<Column> getColumns()
	{
		return columns;
	}

	public void setColumns(List<Column> columns)
	{
		this.columns = columns;
	}	
	
	@Override
	public List<? extends AutoCompleteItem> getSubSuggestions()
	{
		return new ArrayList<AutoCompleteItem>(columns);
	}

	@Override
	public String getAutoCompletion()
	{
		return getSchemaName()+"."+getName();
	}
}
