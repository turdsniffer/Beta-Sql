package com.swingautocompletion.main;

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
}
