package com.betadb.gui.util;

import com.betadb.gui.icons.Icon;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class IconListCellRenderer extends JLabel
                       implements ListCellRenderer  {

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{	
		if(value != null)
		{
			setIcon(Icon.getIcon(value.getClass()));
			setText(value.toString());
		}
        return this;
	}

}
