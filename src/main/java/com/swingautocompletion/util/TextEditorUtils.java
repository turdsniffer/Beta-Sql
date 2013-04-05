package com.swingautocompletion.util;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 * @author parmstrong
 */
public class TextEditorUtils
{
	private static final List WORD_SEPARATORS = Lists.newArrayList(' ', '.', '\n', '\t', ',', ';', '!', '?', '\'', '(', ')', '[', ']', '\"', '{', '}', '/', '\\', '<', '>');
	public static enum ExpansionDirection{
		LEFT,
		RIGHT,
		BOTH
	}
	
	
	
	/**
	 * this method will get the current block of text that the caret is within.  The block is determined by 
	 * blank lines on top and bottom or beginning or ending of string.
	 * @param textComponent
	 * @return 
	 */
	public static String getCurrentTextBlock(JTextComponent textComponent)
	{
		// Compile the pattern
		String patternStr = "^\\s*$";
		Pattern pattern = Pattern.compile(patternStr, Pattern.MULTILINE);
		String text = textComponent.getText();
		Matcher matcher = pattern.matcher(text);
		int caretPosition = textComponent.getCaretPosition();
		// Read the paragraphs
		int prevBlankLineIndex = 0;
		while (matcher.find())
		{
			if (matcher.start() >= caretPosition)
				return textComponent.getText().substring(prevBlankLineIndex, matcher.start());
			prevBlankLineIndex = matcher.start();
		}
		return text.substring(prevBlankLineIndex, text.length());
	}

	public static String getCurrentWord(Pair<Integer, Integer> wordBounds, JTextComponent textComponent)
	{
            try 
            {
                return textComponent.getDocument().getText(wordBounds.getFirst(), wordBounds.getSecond()-wordBounds.getFirst());
            } catch (BadLocationException ex) {
                throw new IndexOutOfBoundsException("Error getting word part location not found");
            }
		
	}

	/**
	 * This will return the word that the caret is in or right next to.
	 * @param textComponent
	 * @return 
	 */
	public static String getCurrentWord(JTextComponent textComponent)
	{
		return getCurrentWord(textComponent, WORD_SEPARATORS);
	}

	public static String getCurrentWord(JTextComponent textComponent, List wordSeparators)
	{
		Pair<Integer, Integer> wordBounds = getWordBounds(textComponent, wordSeparators);
		return textComponent.getText().substring(wordBounds.getFirst(), wordBounds.getSecond()).trim();
	}

	public static Pair<Integer, Integer> getWordBounds(JTextComponent textComponent)
	{
		return getWordBounds(textComponent, WORD_SEPARATORS);
	}
	
	public static Pair<Integer, Integer> getWordBounds(JTextComponent textComponent, List wordSeparators)
	{
		return getWordBounds(textComponent, wordSeparators, ExpansionDirection.BOTH);
	}
	
	public static Pair<Integer, Integer> getWordBounds(JTextComponent textComponent, ExpansionDirection expansionDirection)
	{
		return getWordBounds(textComponent, WORD_SEPARATORS, expansionDirection);
	}
	
	public static Pair<Integer, Integer> getWordBounds(JTextComponent textComponent, List wordSeparators, ExpansionDirection expansionDirection)
	{
                try 
                {
		int startPosition = textComponent.getCaretPosition();
		int endPosition = textComponent.getCaretPosition();
                Document document = textComponent.getDocument();
                
		while (startPosition > 0 && (expansionDirection == ExpansionDirection.BOTH || expansionDirection == ExpansionDirection.LEFT))
		{            
                    startPosition--;               
                    char curChar = document.getText(startPosition,1).charAt(0);        
                
                    if (wordSeparators.contains(curChar))
                    {
                            startPosition++;
                            break;
                    }          
		}
		
		while (endPosition < document.getLength() && (expansionDirection == ExpansionDirection.BOTH || expansionDirection == ExpansionDirection.RIGHT))
		{			
			char curChar = document.getText(endPosition,1).charAt(0);
			if (wordSeparators.contains(curChar))
				break;
			endPosition++;
		}
                return new Pair<Integer, Integer>(startPosition, endPosition);
                } catch (BadLocationException ex) {
                     throw new IndexOutOfBoundsException("Error getting word part location not found");
                }
		
		
	}

	public static void highlightWord(JTextComponent textComponent, String word)
	{			
		removeHighlights(textComponent);
		if(word==null || word.trim().length() == 0)
			return;
		try
		{
			Highlighter highlighter = textComponent.getHighlighter();
			Document doc = textComponent.getDocument();
			String text = doc.getText(0, doc.getLength());
			int pos = 0;
			Pattern p = Pattern.compile("\\b\\Q"+word+"\\E\\b");
			Matcher matcher = p.matcher(text);
			// Search for pattern
			while (matcher.find())
			{
				pos = matcher.start();
				// Create highlighter using private painter and apply around pattern
				highlighter.addHighlight(pos, pos + word.length(), myHighlightPainter);				
			}
		}
		catch (BadLocationException e)
		{
		}
	}

	// Removes only our private highlights
	private static void removeHighlights(JTextComponent textComp)
	{
		Highlighter hilite = textComp.getHighlighter();
		Highlighter.Highlight[] hilites = hilite.getHighlights();

		for (int i = 0; i < hilites.length; i++)
		{
			if (hilites[i].getPainter() instanceof wordHighlighter)
			{
				hilite.removeHighlight(hilites[i]);
			}
		}
	}
	
	// An instance of the private subclass of the default highlight painter
	private static Highlighter.HighlightPainter myHighlightPainter = new wordHighlighter(Color.orange);

	// A private subclass of the default highlight painter
	private static class wordHighlighter extends DefaultHighlighter.DefaultHighlightPainter
	{
		public wordHighlighter(Color color)
		{
			super(color);
		}
	}
}
