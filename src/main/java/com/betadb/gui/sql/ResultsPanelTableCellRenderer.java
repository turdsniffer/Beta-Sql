package com.betadb.gui.sql;

import com.betadb.gui.table.util.renderer.ColumnClassTableCellRenderer;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;

/**
 * @author parmstrong
 */
public class ResultsPanelTableCellRenderer extends ColumnClassTableCellRenderer
{
	JButton button = new JButton();

	public ResultsPanelTableCellRenderer()
	{
		button.setBorder(javax.swing.BorderFactory.createEmptyBorder());
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{

		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if(table.convertColumnIndexToModel(column)==0)
		{
			button.setText(value.toString());
			component = button;
		}


		return component;
	}
}
