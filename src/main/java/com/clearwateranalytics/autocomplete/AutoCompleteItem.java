package com.clearwateranalytics.autocomplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author parmstrong
 */
public abstract class AutoCompleteItem implements Comparable<AutoCompleteItem>
{
	public abstract String getAutoCompleteId();
	
	public String getDescription()
	{
		return "";
	}
	
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
