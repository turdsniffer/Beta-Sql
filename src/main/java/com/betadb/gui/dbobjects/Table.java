package com.betadb.gui.dbobjects;



import com.swingautocompletion.main.AutoCompleteItem;
import java.util.ArrayList;
import java.util.List;

/**
 * @author parmstrong
 */
public class Table extends DbObject
{
	private List<Column> columns;
	private List<Index> indexes;	

	public Table()
	{
		columns = new ArrayList<Column>();
	}

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

	public List<Index> getIndexes()
	{
		return indexes;
	}

	public void setIndexes(List<Index> indexes)
	{
		this.indexes = indexes;
	}

	@Override
	public String getAutoCompletion()
	{
		return getSchemaName()+"."+getName();
	}
}
