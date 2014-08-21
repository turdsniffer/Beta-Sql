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

import com.google.common.collect.Lists;
import com.swingautocompletion.util.TextEditorUtils;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.JTextComponent;

/**
 * @author parmstrong
 * This class is responsible for determining what words to try and find sub suggestions for in our autocomplete item list.
 * By default it just grabs the current block of text in the editor splits it on white space and returns that list.
 * That will end up searching for subsuggestions every auto complete item in the block.  This can be overridden to 
 * provide special handling for certain types of editors.  For example in a sql editor if you have aliased a table and 
 * you type alias.(ctrl+space) you only expect to be given subsuggestions of the columns in that table not for all the 
 * columns of any table in the query.
 * 
 */
public class SubSuggestionsWordSearchProvider
{
	public List<AutoCompleteItem> getItemsToSearchForSubSuggestions(JTextComponent textComponent)
	{
		String block = TextEditorUtils.getCurrentTextBlock(textComponent).toLowerCase();
		ArrayList<AutoCompleteItem> items = Lists.newArrayList();

		if(block == null)
			return items;
		String[] words = block.split("\\s|\\.");

		for (String currentWord : words)
			items.add(new SimpleAutoCompleteItem(currentWord));

		return items;
	}
}
