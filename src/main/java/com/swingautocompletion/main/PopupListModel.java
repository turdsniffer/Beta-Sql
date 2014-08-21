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
