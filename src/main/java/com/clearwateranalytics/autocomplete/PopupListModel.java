package com.clearwateranalytics.autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractListModel;

/**
 * @author parmstrong
 */
public class PopupListModel extends AbstractListModel
{
	ArrayList<AutoCompleteItem> items;

	public PopupListModel()
	{
		items = new ArrayList<AutoCompleteItem>();
	}		
	
	@Override
	public int getSize()
	{
		return items.size();
	}
	
	public AutoCompleteItem getItem(int index)
	{
		if(items.isEmpty())
			return null;
		
		return items.get(index);
	}
	
	public void addAll(Collection<AutoCompleteItem> newItems)
	{
		int index = items.size();
		items.addAll(newItems);
		if(items.size() > 0)
			fireIntervalAdded(this, index, items.size()-1);
		
	}
	
	public void add(AutoCompleteItem item)
	{
		int index = items.size();
		items.add(item);
		fireIntervalAdded(this, index, index);
	}
	
	public void removeAllElements()
	{
		int index1 = items.size()-1;
		items.clear();
		if (index1 >= 0) 
			fireIntervalRemoved(this, 0, index1);
	}

	@Override
	public Object getElementAt(int i)
	{
		return items.get(i);
	}
	
	public boolean isEmpty()
	{
		return items.isEmpty();
	}
}
