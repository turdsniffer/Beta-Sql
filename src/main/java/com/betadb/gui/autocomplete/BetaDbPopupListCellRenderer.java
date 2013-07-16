package com.betadb.gui.autocomplete;

import com.swingautocompletion.main.AutoCompleteItem;
import com.swingautocompletion.main.PopupListCellRenderer;
import com.betadb.gui.icons.Icon;
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


		//this.setText("<html>"+item.getAutoCompleteId() +" <FONT size=-2 COLOR=\"#00FFFF\">"+ item.getDescription()+"</FONT></html>");
		this.setText(item.getAutoCompleteId() +" ("+ item.getDescription()+")");
		setIcon(Icon.getIcon(value.getClass()));
			
        return this;
	}
}
