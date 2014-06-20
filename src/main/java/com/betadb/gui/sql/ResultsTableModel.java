package com.betadb.gui.sql;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.swing.table.AbstractTableModel;

/**
 * @author parmstrong
 */
public class ResultsTableModel extends AbstractTableModel
{
	private List<String> columnNames;
	private ArrayList<Object[]> data;
	private List<Class> columnClasses;


	public ResultsTableModel(List<String> columnNames, List<Class> columnClasses, ArrayList<Object[]> data)
	{
		this.columnNames = columnNames;
		this.columnClasses = columnClasses;
		this.data = data;
		addRowCountColumn();
	}

	private void addRowCountColumn()
	{
		columnNames.add(0, "");
		columnClasses.add(0, Integer.class);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if(columnIndex == 0)
			return rowIndex;

		Object obj = data.get(rowIndex)[columnIndex-1];
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
				getLogger(ResultsTableModel.class.getName()).log(Level.SEVERE, "Oh no SQL exception", ex);
			}
		}
		return obj;

	}

	@Override
	public Class getColumnClass(int columnIndex)
	{
		return columnClasses.get(columnIndex) == null ? Object.class : columnClasses.get(columnIndex);
	}


	@Override
	public int getColumnCount()
	{
		return columnNames.size();
	}

	@Override
	public int getRowCount()
	{
		return data.size();
	}

	@Override
	public String getColumnName(int column)
	{
		return columnNames.get(column);
	}
}
