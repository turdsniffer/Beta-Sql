
package com.swingautocompletion.main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author parmstrong
 */
public class LinearSearch implements SearchStrategy
{
	@Override
	public List<AutoCompleteItem> search(String wordPart, List<AutoCompleteItem> items)
	{
		List<AutoCompleteItem> suggestions = new ArrayList<AutoCompleteItem>();
		for (AutoCompleteItem autoCompleteItem : items)
		{
			if (autoCompleteItem.getAutoCompleteId().toLowerCase().startsWith(wordPart.toLowerCase()))
				suggestions.add(autoCompleteItem);
			else
			{
				for (String alternateId : autoCompleteItem.alternateAutoCompeteIds())
				{
					if (alternateId.toLowerCase().startsWith(wordPart.toLowerCase()))
					{
						suggestions.add(autoCompleteItem);
						break;
					}
				}
			}
		}
		return suggestions;
	}
}
