package com.swingautocompletion.util;

/*
 * #%L
 * SwingAutoCompletion
 * %%
 * Copyright (C) 2013 - 2014 SwingAutoComplete
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
