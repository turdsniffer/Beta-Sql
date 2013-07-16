package com.betadb.gui.table.util.renderer;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author parmstrong
 */
public class ColumnClassTableCellRenderer extends DefaultTableCellRenderer
{
	private static Color ZEBRA_STRIPING_COLOR = new Color(225, 230, 244);
	private static final Color SELECTED_TABLE_ROW_COLOR = new Color(49, 106, 197);
	private static final Color SELECTED_TABLE_CELL_COLOR = new Color(115, 162, 222);
	Map<Class, CellClassRenderer> classRendererMap;

	public ColumnClassTableCellRenderer()
	{
		classRendererMap = new HashMap<Class, CellClassRenderer>();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		applyDefaultFormatting(component);

		if (isSelected)
			formatSelectedRow(table, column, row);
		else
			zebraStripRow(component, row);
		createLabelToolTip(component);		
		Component renderedComponent = getRenderedComponent(value, table.getModel().getColumnClass(table.convertColumnIndexToModel(column)), component);
		return renderedComponent;
	}

	private Component getRenderedComponent(Object value, Class columnClass, Component component)
	{
		CellClassRenderer renderer = classRendererMap.get(columnClass);
		if (value == null && component instanceof JLabel)
			((JLabel)component).setText("");
		else if (renderer != null)
			component = renderer.render(value, component);
		return component;
	}

	private void zebraStripRow(Component component, int row)
	{
		component.setForeground(Color.BLACK);
		if (row % 2 != 0)
			component.setBackground(ZEBRA_STRIPING_COLOR);
		else
			component.setBackground(Color.WHITE);
	}

	public void addClassRenderer(Class clas, CellClassRenderer renderer)
	{
		classRendererMap.put(clas, renderer);
	}

	private void applyDefaultFormatting(Component component)
	{
		setIcon(null);
		component.setForeground(Color.BLACK);
		component.setBackground(Color.WHITE);
	}

	private void createLabelToolTip(Component c)
	{
		if (c instanceof JLabel)
		{
			JLabel label = (JLabel) c;
			String text = label.getText();
			if (text != null && text.trim().length() <= 0)
				text = null;
			label.setToolTipText("<html>" + text + "</html>");
		}
	}

	private void formatSelectedRow(JTable table, int column, int row)
	{
		this.setForeground(Color.WHITE);
		if ((table.getSelectedColumn() == column) && (table.getSelectedRow() == row))
			this.setBackground(SELECTED_TABLE_CELL_COLOR);
		else
			this.setBackground(SELECTED_TABLE_ROW_COLOR);
	}
}
