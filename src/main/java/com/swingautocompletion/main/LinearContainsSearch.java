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
