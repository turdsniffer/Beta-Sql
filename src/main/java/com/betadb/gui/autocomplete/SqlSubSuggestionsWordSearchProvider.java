package com.betadb.gui.autocomplete;


import com.swingautocompletion.util.TextEditorUtils;
import com.google.common.collect.Lists;
import com.swingautocompletion.main.SubSuggestionsWordSearchProvider;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.JTextComponent;

/**
 * @author parmstrong
 */
public class SqlSubSuggestionsWordSearchProvider extends SubSuggestionsWordSearchProvider
{
	private static final List WORD_SEPARATORS = Lists.newArrayList(' ', '\n', '\t', ',', ';', '!', '?', '\'', '(', ')', '[', ']', '\"', '{', '}', '/', '\\', '<', '>');
	
	
	/**
	 * This method determines the items we grab sub suggestions from.  normally this will just grab all the 
	 * subsuggestions for any auto complete item in the block and then begin filtering them as you type.  
	 * However for our editor if we type <tablealias>. and then hit ctrl space it should only find subsuggestions for the
	 * table we have aliased.  In this way we don't get suggestions for every table in the sql block but only the table we
	 * are completing for.
	 * @param textComponent
	 * @return 
	 */
	
	@Override
	public String[] getWordsToSearchForSubSuggestions(JTextComponent textComponent)
	{
		String currentWord = TextEditorUtils.getCurrentWord(textComponent, WORD_SEPARATORS);
		if (currentWord.endsWith(".") && currentWord.length() > 1)
		{			
			currentWord = currentWord.substring(0, currentWord.length()-1);
			Pattern p = Pattern.compile("\\b"+currentWord+"\\b");
			
			String currentTextBlock = TextEditorUtils.getCurrentTextBlock(textComponent);
			Matcher matcher = p.matcher(currentTextBlock);
			if(matcher.find())
			{	
				int firstOccurance = matcher.start();

				currentTextBlock = currentTextBlock.substring(0,firstOccurance);
				String[] split = currentTextBlock.split("\\s|\\.");
				if(split.length > 0)
				{

					String [] word = new String[1];
					if(split[split.length-1].equalsIgnoreCase("as"))
						word[0] = split[split.length-2];
					else
						word[0] = split[split.length-1];
					return word;				
				}	
			}
		}
		return super.getWordsToSearchForSubSuggestions(textComponent);
	}
}
