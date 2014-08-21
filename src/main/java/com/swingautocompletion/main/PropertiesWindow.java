package com.swingautocompletion.main;

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

	public PropertiesWindow(final AutoCompletePopup parent)
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
				point.x=point.x+parent.getWidth();
				
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
