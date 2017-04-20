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
	private List<ForeignKey> foreignKeys;
	private List<PrimaryKey> primaryKeys;


	public Table()
	{
		columns = new ArrayList<>();
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
		return new ArrayList<>(columns);
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

	public List<ForeignKey> getForeignKeys()
	{
		return foreignKeys;
	}

	public void setForeignKeys(List<ForeignKey> foreignKeys)
	{
		this.foreignKeys = foreignKeys;
	}

	public List<PrimaryKey> getPrimaryKeys()
	{
		return primaryKeys;
	}

	public void setPrimaryKeys(List<PrimaryKey> primaryKeys)
	{
		this.primaryKeys = primaryKeys;
	}
}
