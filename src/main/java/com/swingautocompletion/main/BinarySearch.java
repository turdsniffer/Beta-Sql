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
