package com.swingautocompletion.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author parmstrong
 * This is the typical search in the list of auto complete items.  It does a binary search on words starting with the 
 * wordPart.  Once it finds one it searches above and below to find all the suggestions that match.
 */
public class BinarySearch implements SearchStrategy
{
	@Override
	public List<AutoCompleteItem> search(String wordPart, List<AutoCompleteItem> items)
	{
		wordPart = wordPart.toLowerCase();
		if (wordPart.trim().isEmpty())
			return items;

		List<AutoCompleteItem> retVal = new ArrayList<AutoCompleteItem>();
		SimpleAutoCompleteItem item = new SimpleAutoCompleteItem(wordPart);
		int searchIndex = Collections.binarySearch(items, item, new Comparator<AutoCompleteItem>()
		{
			@Override
			public int compare(AutoCompleteItem listItem, AutoCompleteItem item)
			{
				boolean match = listItem.getAutoCompleteId().toLowerCase().startsWith(item.getAutoCompleteId().toLowerCase());
				return match ? 0 : listItem.getAutoCompleteId().toLowerCase().compareTo(item.getAutoCompleteId().toLowerCase());
			}
		});
		if (searchIndex < 0)
			return retVal;
		AutoCompleteItem possibleMatch = items.get(searchIndex);
		if (possibleMatch.getAutoCompleteId().toLowerCase().startsWith(wordPart))
		{

			while (searchIndex >= 0 && items.get(searchIndex).getAutoCompleteId().toLowerCase().startsWith(wordPart))
				searchIndex--;
			searchIndex++;
			while (searchIndex < items.size()
				   && items.get(searchIndex).getAutoCompleteId().toLowerCase().startsWith(wordPart))
			{
				retVal.add(items.get(searchIndex));
				searchIndex++;
			}
		}
		return retVal;
	}
}
