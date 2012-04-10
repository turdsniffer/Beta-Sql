package com.clearwateranalytics.betadb.gui.autocomplete;

import com.clearwateranalytics.autocomplete.AutoCompleteItem;
import com.clearwateranalytics.autocomplete.PopupListCellRenderer;
import com.clearwateranalytics.betadb.gui.icons.Icon;
import java.awt.Component;
import javax.swing.JList;


/**
 * @author parmstrong
 */
public class BetaDbPopupListCellRenderer extends PopupListCellRenderer
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
		setIcon(Icon.getIcon(value.getClass()));
			
        return this;
	}
}
