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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author parmstrong
 */
public abstract class AutoCompleteItem implements Comparable<AutoCompleteItem>
{
	/**
	 * text to search for when a user begins to type
	 */
	public abstract String getAutoCompleteId();

	public List<String> alternateAutoCompeteIds()
	{
		return Collections.EMPTY_LIST;
	}
	
	public String getDescription()
	{
		return "";
	}

	/**
	 * text to insert upon selecting this auto complete item
	 */
	public String getAutoCompletion()
	{
		return getAutoCompleteId();
	}

	/**
	 * extra display information about this auto complete item to be displayed when highlighted.	 
	 */
	public Map<String,String> getProperties()
	{
		return new HashMap<String, String>();
	}
	
	public List<? extends AutoCompleteItem> getSubSuggestions()
	{
		return new ArrayList<AutoCompleteItem>();
	}
	
	@Override
	public int compareTo(AutoCompleteItem other)
	{
		return this.getAutoCompleteId().toLowerCase().compareTo(other.getAutoCompleteId().toLowerCase());		
	}

	public void onSelectAction()
	{}
}
