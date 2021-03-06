package com.betadb.gui.sql;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;

/**
 * @author parmstrong
 */
public class RowNumberColumnMouseListener extends MouseAdapter
{
	@Override
	public void mouseClicked(MouseEvent event)
	{
		JTable table = (JTable) event.getSource();
		int row = table.rowAtPoint(event.getPoint());
		int col = table.columnAtPoint(event.getPoint());
		if(table.convertColumnIndexToModel(col)==0)//They have clicked on the row number button
		{			
			table.setRowSelectionInterval(row, row);
			table.setColumnSelectionInterval(0, table.getColumnCount()-1);			
		}
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{
		JTable table = (JTable) event.getSource();
		int row = table.rowAtPoint(event.getPoint());
		int col = table.columnAtPoint(event.getPoint());
		if(table.convertColumnIndexToModel(col)==0)//They have clicked on the row number button
		{				
			table.addRowSelectionInterval(row-1, row);
			table.setColumnSelectionInterval(0, table.getColumnCount()-1);
				
		}
	}
}
