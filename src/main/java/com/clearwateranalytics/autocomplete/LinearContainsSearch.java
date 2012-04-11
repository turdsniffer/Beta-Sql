package com.clearwateranalytics.autocomplete;

import java.util.ArrayList;
import java.util.List;

/**
 * @author parmstrong
 * This search performs a fuzzy search for any auto complete item that has the word part somewhere in the name of the item
 * for example if the word part is dog it will match dogbert and catdog.  Obviously this will be slower than the binary search
 * but often more convenient;
 */
public class LinearContainsSearch implements SearchStrategy
{
	@Override
	public List<AutoCompleteItem> search(String wordPart, List<AutoCompleteItem> items)
	{
		List<AutoCompleteItem> suggestions = new ArrayList<AutoCompleteItem>();
		for (AutoCompleteItem autoCompleteItem : items)
		{
			if (autoCompleteItem.getAutoCompleteId().toLowerCase().contains(wordPart.toLowerCase()))
				suggestions.add(autoCompleteItem);
		}
		return suggestions;
	}
}
