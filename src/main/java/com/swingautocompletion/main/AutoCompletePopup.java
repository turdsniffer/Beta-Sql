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
import com.swingautocompletion.util.Pair;
import com.swingautocompletion.util.TextEditorUtils;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
 * @author parmstrong
 */
public class AutoCompletePopup extends JWindow
{
	private JList list;
	private PopupListModel model;
	private List<AutoCompleteItem> items = new ArrayList<AutoCompleteItem>();
	private Map<String, AutoCompleteItem> autoCompleteIdToItemMap;
	private Set<AutoCompleteItem> subSuggestions;
	private JTextComponent textComponent;
	private List<AutoCompleteHandler> autoCompleteHandlers;
	private SearchStrategy searchStrategy;
	private SubSuggestionsWordSearchProvider subSuggestionsWordSearchProvider;
	private SearchTermProvider searchTermProvider;
	private PropertiesWindow propertiesWindow;
	private final Dimension dimension = new Dimension(300, 200);

	public AutoCompletePopup(JTextComponent textComponent)
	{
		this(textComponent, new PopupListCellRenderer(), new SubSuggestionsWordSearchProvider(), new DefaultSearchTermProvider());
	}

	public AutoCompletePopup(JTextComponent textComponent, PopupListCellRenderer popupListCellRenderer, SubSuggestionsWordSearchProvider subSuggestionsWordSearchProvider, SearchTermProvider searchTermProvider)
	{
		this.propertiesWindow = new PropertiesWindow(this);
		this.subSuggestionsWordSearchProvider = subSuggestionsWordSearchProvider;
		this.autoCompleteIdToItemMap = new HashMap<String, AutoCompleteItem>();
		this.subSuggestions = new TreeSet<AutoCompleteItem>();
		this.searchStrategy = new LinearSearch();
		this.searchTermProvider = searchTermProvider;

		autoCompleteHandlers = new ArrayList<AutoCompleteHandler>();
		this.textComponent = textComponent;
		addListenersToTextComponent();
		this.setAlwaysOnTop(true);
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		model = new PopupListModel();
		list = new JList(model);
		list.setCellRenderer(popupListCellRenderer);
		JScrollPane jScrollPane = new JScrollPane(list);
		list.setBackground(Color.LIGHT_GRAY);
		list.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent lse)
			{
				if (!lse.getValueIsAdjusting())
				{
					int selectedIndex = list.getSelectedIndex();
					if (selectedIndex < 0)
						return;
					AutoCompleteItem item = model.getItem(selectedIndex);
					if (item != null)
						propertiesWindow.showProperties(item);
				}
			}
		});

		this.setPreferredSize(dimension);
		this.add(jScrollPane);
		this.doLayout();
		pack();
	}

	private void addListenersToTextComponent()
	{
		KeyAdapter keyAdapter = new KeyAdapter()
		{

			public void keyPressed(KeyEvent e)
			{
				if (e.isConsumed()) return;

				if (e.getKeyCode() == KeyEvent.VK_SPACE && (e.isControlDown() || e.isShiftDown()))
					initiateUpdate(e);
				if (AutoCompletePopup.this.isVisible())
				{
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						insertSelectedItem();
						e.consume();
					}
					else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					{
						moveDown();
						e.consume();
					}
					else if (e.getKeyCode() == KeyEvent.VK_UP)
					{
						moveUp();
						e.consume();
					}
					else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						AutoCompletePopup.this.setVisible(false);
						e.consume();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.isConsumed()) return;

				if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyCode() == KeyEvent.VK_PERIOD)
					initiateUpdate(e);
			}

			private void initiateUpdate(KeyEvent e)
			{
				if (e.isShiftDown() && e.isControlDown())
					searchStrategy = new LinearContainsSearch();
				else
					searchStrategy = new LinearSearch();

				addSubSuggestions();
				updateAutoComplete();
				e.consume();

			}
		};

		FocusListener focusListener = new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				AutoCompletePopup.this.setVisible(false);
			}
		};

		textComponent.addKeyListener(keyAdapter);
		textComponent.addFocusListener(focusListener);
	}

	private void addSubSuggestions()
	{
		subSuggestions.clear();
		List<AutoCompleteItem> itemsToMatch = subSuggestionsWordSearchProvider.getItemsToSearchForSubSuggestions(textComponent);

		for (AutoCompleteItem autoCompleteItem : itemsToMatch)
		{
			AutoCompleteItem match = autoCompleteIdToItemMap.get(autoCompleteItem.getAutoCompleteId().toLowerCase());
			if (match != null)
				subSuggestions.addAll(match.getSubSuggestions());
		}
	}

	public void setAutoCompletePossibilties(List<? extends AutoCompleteItem> items)
	{
		this.items = new ArrayList<AutoCompleteItem>(items);
		for (AutoCompleteItem autoCompleteItem : items)
		{
			autoCompleteIdToItemMap.put(autoCompleteItem.getAutoCompleteId().toLowerCase(), autoCompleteItem);
			for (String alternateId : autoCompleteItem.alternateAutoCompeteIds())
				autoCompleteIdToItemMap.put(alternateId.toLowerCase(), autoCompleteItem);
		}
		this.sortSuggestions();
	}

	public void addAutoCompletePossibility(AutoCompleteItem autoCompleteItem)
	{
		this.items.add(autoCompleteItem);
		this.sortSuggestions();
	}

	private void sortSuggestions()
	{
		Collections.sort(this.items);
	}

	private void updatePosition()
	{
		try
		{
			Rectangle rect = textComponent.modelToView(textComponent.getCaretPosition());
			Point location = textComponent.getLocationOnScreen();
			location.x += rect.x;
			location.y += rect.y + 15;
			this.setLocation(location);
		}
		catch (BadLocationException ex)
		{
			Logger.getLogger(AutoCompletePopup.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void updateAutoComplete()
	{
		updatePosition();
		String wordPart = searchTermProvider.getSearchTerm(textComponent);
		model.removeAllElements();
		List<AutoCompleteItem> foundMatches = searchStrategy.search(wordPart, new ArrayList<AutoCompleteItem>(subSuggestions));
		foundMatches.addAll(searchStrategy.search(wordPart, items));

		model.addAll(foundMatches);
		list.setSelectedIndex(0);
		if (!model.isEmpty())
			this.setVisible(true);
		else
			this.setVisible(false);
	}

	private void insertSelectedItem()
	{
		AutoCompleteItem item = (AutoCompleteItem) list.getSelectedValue();
		ArrayList wordSeparators = Lists.newArrayList(TextEditorUtils.DEFAULT_WORD_SEPARATORS);
		if (item.getAutoCompletion().contains("."))//This is a bit of a hack for auto completions that have a . in them.  This is not a robust solution for this.
			wordSeparators.remove(Character.valueOf('.'));

		Pair<Integer, Integer> replacementWordBounds = TextEditorUtils.getWordBounds(textComponent, wordSeparators, TextEditorUtils.ExpansionDirection.LEFT);

		String autoCompletion = item.getAutoCompletion();

		textComponent.setSelectionStart(replacementWordBounds.getFirst());
		textComponent.setSelectionEnd(replacementWordBounds.getSecond());
		textComponent.replaceSelection(autoCompletion);

		notifyAutoCompleteHandlers(item);
		this.setVisible(false);
	}

	private void moveUp()
	{
		if (model.getSize() < 1) return;
		int current = list.getSelectedIndex();
		int newIndex = Math.max(0, current - 1);
		list.setSelectionInterval(newIndex, newIndex);
		list.scrollRectToVisible(list.getCellBounds(newIndex, newIndex));
	}

	private void moveDown()
	{
		if (model.getSize() < 1) return;
		int current = list.getSelectedIndex();
		int newIndex = Math.min(model.getSize() - 1, current + 1);
		list.setSelectionInterval(newIndex, newIndex);
		list.scrollRectToVisible(list.getCellBounds(newIndex, newIndex));
	}

	public void addAutoCompleteHandler(AutoCompleteHandler handler)
	{
		autoCompleteHandlers.add(handler);
	}

	private void notifyAutoCompleteHandlers(AutoCompleteItem autoCompleteItem)
	{
		for (AutoCompleteHandler autoCompleteHandler : autoCompleteHandlers)
			autoCompleteHandler.handle(autoCompleteItem);
	}

}
