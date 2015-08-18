package com.betadb.gui.autocomplete;

import com.swingautocompletion.util.TextEditorUtils;
import static com.google.common.collect.Lists.newArrayList;
import com.swingautocompletion.main.AutoCompleteItem;
import com.swingautocompletion.main.SearchTermProvider;
import com.swingautocompletion.main.SimpleAutoCompleteItem;
import com.swingautocompletion.main.SubSuggestionsWordSearchProvider;
import com.swingautocompletion.util.Pair;
import static com.swingautocompletion.util.TextEditorUtils.getCurrentTextBlock;
import static com.swingautocompletion.util.TextEditorUtils.getCurrentWord;
import static com.swingautocompletion.util.TextEditorUtils.getWordBounds;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.compile;
import javax.swing.text.JTextComponent;

/**
 * @author parmstrong
 */
public class SqlSubSuggestionsWordSearchProvider extends SubSuggestionsWordSearchProvider implements SearchTermProvider
{
	private static final List WORD_SEPARATORS = newArrayList(' ', '\n', '\t', ',', ';', '!', '?', '\'', '(', ')', '[', ']', '\"', '{', '}', '/', '\\', '<', '>');
	private static final List ALIAS_SEPARATORS = newArrayList(' ', '.', '\n', '\t', ',', ';', '!', '?', '\'', '(', ')', '[', ']', '\"', '{', '}', '/', '\\', '<', '>');



	@Override
	public String getSearchTerm(JTextComponent textComponent)
	{
		Pair<Integer, Integer> currentWordBounds;
		if(isCurrentTermAnAlias(textComponent))
			currentWordBounds = getWordBounds(textComponent, ALIAS_SEPARATORS, TextEditorUtils.ExpansionDirection.LEFT);
		else
			currentWordBounds = getWordBounds(textComponent, WORD_SEPARATORS, TextEditorUtils.ExpansionDirection.LEFT);
		return getCurrentWord(currentWordBounds, textComponent);
	}


	/**
	 * This method determines the items we grab sub suggestions from. normally this will just grab all the subsuggestions for any auto complete item in the block and then begin filtering them as you
	 * type. However for our editor if we type <tablealias>. and then hit ctrl space it should only find subsuggestions for the table we have aliased. In this way we don't get suggestions for every
	 * table in the sql block but only the table we are completing for.
	 *
	 * @param textComponent
	 * @return
	 */
	@Override
	public List<AutoCompleteItem> getItemsToSearchForSubSuggestions(JTextComponent textComponent)
	{
		String currentWord = getCurrentWord(textComponent, WORD_SEPARATORS);
		if (currentWord.endsWith(".") && currentWord.length() > 1)
			return getItemsForAlias(textComponent, currentWord);
		return getItemsDefault(textComponent);
	}

	private List<AutoCompleteItem> getItemsForAlias(JTextComponent textComponent, String currentWord)
	{
		currentWord = currentWord.substring(0, currentWord.length() - 1);
		Pattern p = compile("\\b" + currentWord + "\\b", Pattern.CASE_INSENSITIVE);
		ArrayList<AutoCompleteItem> alias = newArrayList();

		String currentTextBlock = getCurrentTextBlock(textComponent);
		Matcher matcher = p.matcher(currentTextBlock);
		if (matcher.find())
		{
			int firstOccurance = matcher.start();

			currentTextBlock = currentTextBlock.substring(0, firstOccurance);
			String[] split = currentTextBlock.split("\\s|\\.");
			if (split.length > 0)
			{
				if (split[split.length - 1].equalsIgnoreCase("as"))
					alias.add(new SimpleAutoCompleteItem(split[split.length - 2]));
				else
					alias.add(new SimpleAutoCompleteItem(split[split.length - 1]));
			}
		}
		return alias;
	}

	private boolean isCurrentTermAnAlias(JTextComponent textComponent)
	{
		String currentWord = getCurrentWord(textComponent, WORD_SEPARATORS);
		if(!currentWord.contains("."))
			return false;
		String alias = currentWord.substring(0, currentWord.indexOf("."));
		Pattern p = compile("\\s" + alias + "\\s");

		String currentTextBlock = getCurrentTextBlock(textComponent);
		Matcher matcher = p.matcher(currentTextBlock);
		return matcher.find();
	}

	private List<AutoCompleteItem> getItemsDefault(JTextComponent textComponent)
	{
		String block = getCurrentTextBlock(textComponent).toLowerCase();
		ArrayList<AutoCompleteItem> items = newArrayList();

		if (block == null)
			return items;
		String[] words = block.split("\\s");

		for (String currentWord : words)
			items.add(new SimpleAutoCompleteItem(currentWord));

		return items;
	}
}
