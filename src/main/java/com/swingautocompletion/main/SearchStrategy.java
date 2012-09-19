package com.swingautocompletion.main;

import java.util.List;

/**
 * @author parmstrong
 */
public interface SearchStrategy
{
	public List<AutoCompleteItem> search(String wordPart, List<AutoCompleteItem> items);
}
