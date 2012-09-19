package com.betadb.gui.dbobjects;

/**
 * @author parmstrong
 */
public class IndexColumn
{
	private int indexId;
	private int objectId;
	private int indexColumnId;
	private int columnId;

	public int getIndexId()
	{
		return indexId;
	}

	public void setIndexId(int indexId)
	{
		this.indexId = indexId;
	}

	public int getObjectId()
	{
		return objectId;
	}

	public void setObjectId(int objectId)
	{
		this.objectId = objectId;
	}

	public int getIndexColumnId()
	{
		return indexColumnId;
	}

	public void setIndexColumnId(int indexColumnId)
	{
		this.indexColumnId = indexColumnId;
	}

	public int getColumnId()
	{
		return columnId;
	}

	public void setColumnId(int columnId)
	{
		this.columnId = columnId;
	}
}
