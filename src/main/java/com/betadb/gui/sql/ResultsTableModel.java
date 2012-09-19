package com.betadb.gui.sql;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 * @author parmstrong
 */
public class ResultsTableModel extends AbstractTableModel
{
	private String[] columnNames;
	private ArrayList<Object[]> data;
	private Class[] columnClasses;


	public ResultsTableModel(String[] columnNames, Class[] columnClasses, ArrayList<Object[]> data)
	{
		this.columnNames = columnNames;
		this.columnClasses = columnClasses;
		this.data = data;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Object obj = data.get(rowIndex)[columnIndex];
		if (obj instanceof Clob)
		{
			try
			{
				Clob clob = ((Clob) obj);
				int length = 0;
				if (clob.length() > Integer.MAX_VALUE)
					length = Integer.MAX_VALUE;
				else
					length = (int) clob.length();
				return clob.getSubString(1, length);
			}
			catch (SQLException ex)
			{
				Logger.getLogger(ResultsTableModel.class.getName()).log(Level.SEVERE, "Oh no SQL exception", ex);
			}
		}
		return obj;

	}

	@Override
	public Class getColumnClass(int columnIndex)
	{
		return columnClasses[columnIndex] == null ? Object.class : columnClasses[columnIndex];
	}


	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public int getRowCount()
	{
		return data.size();
	}

	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}
}
