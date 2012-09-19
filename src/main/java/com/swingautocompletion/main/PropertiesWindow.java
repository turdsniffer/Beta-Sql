package com.swingautocompletion.main;

import com.swingautocompletion.util.ZebraTableRenderer;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.table.AbstractTableModel;

/**
 * @author parmstrong
 */
public class PropertiesWindow extends JWindow
{
	PropertiesTableModel propertiesTableModel;

	public PropertiesWindow(AutoCompletePopup parent)
	{
		parent.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentHidden(ComponentEvent e)
			{
				PropertiesWindow.this.setVisible(false);
			}

			@Override
			public void componentMoved(ComponentEvent ce)
			{
				Point point = ce.getComponent().getLocation();
				point.x=point.x+200;
				
				PropertiesWindow.this.setLocation(point);			
			}
		});

		propertiesTableModel = new PropertiesTableModel();		
		JTable propertiesTable = new JTable(propertiesTableModel);
		propertiesTable.setDefaultRenderer(Object.class, new ZebraTableRenderer());
		JScrollPane jScrollPane = new JScrollPane(propertiesTable);
		this.setPreferredSize(new Dimension(300, 200));
		this.add(jScrollPane);
		this.doLayout();
		pack();
	}

	public void showProperties(AutoCompleteItem item)
	{		
		System.out.println("update: " + item.getAutoCompletion());
		propertiesTableModel.setItem(item);
		propertiesTableModel.fireTableDataChanged();
		if (!propertiesTableModel.properties.isEmpty())
			this.setVisible(true);
	}

	private enum Col
	{
		Name,
		Value
	}

	private class PropertiesTableModel extends AbstractTableModel
	{
		private Map<String, String> properties;
		private List<String> propertyKeys;

		public PropertiesTableModel()
		{
			properties = new HashMap<String, String>();
			propertyKeys = new ArrayList<String>();
		}

		@Override
		public int getRowCount()
		{
			return propertyKeys.size();
		}

		@Override
		public int getColumnCount()
		{
			return 2;
		}

		@Override
		public String getColumnName(int col)
		{
			return Col.values()[col].name();
		}

		@Override
		public Object getValueAt(int row, int col)
		{
			Col column = Col.values()[col];
			switch (column)
			{
				case Name:
					return propertyKeys.get(row);
				case Value:
					return properties.get(propertyKeys.get(row));
			}
			return null;
		}

		public void setItem(AutoCompleteItem item)
		{		
			this.properties = item.getProperties();
			this.propertyKeys.clear();
			this.propertyKeys.addAll(item.getProperties().keySet());
			Collections.sort(propertyKeys);
		}
	}
}
