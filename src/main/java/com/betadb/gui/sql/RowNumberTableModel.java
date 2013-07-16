
package com.betadb.gui.sql;

import javax.swing.table.AbstractTableModel;

/**
 * @author parmstrong
 */
public class RowNumberTableModel extends AbstractTableModel
{
	private int rowCount;

	public RowNumberTableModel(int rowCount)
	{
		this.rowCount = rowCount;
	}

	@Override
	public int getRowCount()
	{
		return rowCount;
	}

	@Override
	public int getColumnCount()
	{
		return 1;
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		return row;
	}



}
