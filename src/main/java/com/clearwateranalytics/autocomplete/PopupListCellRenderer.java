package com.clearwateranalytics.autocomplete;


import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;


/**
 * @author parmstrong
 */
public class PopupListCellRenderer extends DefaultListCellRenderer
{
	@Override
	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus)
	{		
		
		
		if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }		
		
		AutoCompleteItem item = (AutoCompleteItem)value;
		
		this.setText(item.getAutoCompleteId() +" "+ item.getDescription());	
			
        return this;
	}
}
