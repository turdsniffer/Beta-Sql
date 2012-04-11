package com.clearwateranalytics.autocomplete;

import com.clearwateranalytics.autocomplete.util.TextEditorUtils;
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
	public String[] getWordsToSearchForSubSuggestions(JTextComponent textComponent)
	{
		String block = TextEditorUtils.getCurrentTextBlock(textComponent);

		if(block == null)
			return new String[0];
		return block.split("\\s");		
	}
}
