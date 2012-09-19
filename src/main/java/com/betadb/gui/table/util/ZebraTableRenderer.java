package com.betadb.gui.table.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author parmstrong
 */
public class ZebraTableRenderer extends DefaultTableCellRenderer
{
	private static Color ZEBRA_STRIPING_COLOR = new Color(225, 230, 244);
	private static final Color SELECTED_TABLE_ROW_COLOR = new Color(49,106,197);
    private static final Color SELECTED_TABLE_CELL_COLOR = new Color(115, 162, 222);
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component= super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		
		if(isSelected)
			formatSelectedRow(table, column, row);
		else 
		{
			component.setForeground(Color.BLACK);
			if(row % 2 == 1)
				component.setBackground(ZEBRA_STRIPING_COLOR);
			else
				component.setBackground(Color.WHITE);
		}

		
		return component;
	}
	
	private void formatSelectedRow(JTable table, int column, int row)
	{
		this.setForeground(Color.WHITE);
		if ((table.getSelectedColumn() == column) && (table.getSelectedRow() == row)) this.setBackground(SELECTED_TABLE_CELL_COLOR);
		else this.setBackground(SELECTED_TABLE_ROW_COLOR);
	}
	
}
