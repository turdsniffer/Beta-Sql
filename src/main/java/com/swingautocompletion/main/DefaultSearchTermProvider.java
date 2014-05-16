
package com.swingautocompletion.main;

import com.swingautocompletion.util.Pair;
import com.swingautocompletion.util.TextEditorUtils;
import javax.swing.text.JTextComponent;

/**
 * @author parmstrong
 */
public class DefaultSearchTermProvider implements SearchTermProvider
{

	@Override
	public String getSearchTerm(JTextComponent textComponent)
	{
		Pair<Integer, Integer> currentWordBounds = TextEditorUtils.getWordBounds(textComponent, TextEditorUtils.ExpansionDirection.LEFT);
		return TextEditorUtils.getCurrentWord(currentWordBounds, textComponent);
	}

}
