package com.swingautocompletion.main;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
	private List<AutoCompleteItem> items;
	private Set<AutoCompleteItem> subSuggestions;
	private JTextComponent textComponent;
	private Pair<Integer, Integer> currentWordBounds;
	private List<AutoCompleteHandler> autoCompleteHandlers;
	private SearchStrategy searchStrategy;
	private SubSuggestionsWordSearchProvider subSuggestionsWordSearchProvider;
	private PropertiesWindow propertiesWindow;

	public AutoCompletePopup(JTextComponent textComponent)
	{
		this(textComponent, new PopupListCellRenderer(), new SubSuggestionsWordSearchProvider());
	}

	public AutoCompletePopup(JTextComponent textComponent, PopupListCellRenderer popupListCellRenderer, SubSuggestionsWordSearchProvider subSuggestionsWordSearchProvider)
	{		
		this.propertiesWindow = new PropertiesWindow(this);
		this.subSuggestionsWordSearchProvider = subSuggestionsWordSearchProvider;
		subSuggestions = new TreeSet<AutoCompleteItem>();
		searchStrategy = new BinarySearch();
		autoCompleteHandlers = new ArrayList<AutoCompleteHandler>();
		this.textComponent = textComponent;
		addListenersToTextComponent();
		this.setAlwaysOnTop(false);
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		model = new PopupListModel();
		list = new JList(model);
		list.setCellRenderer(popupListCellRenderer);
		JScrollPane jScrollPane = new JScrollPane(list);
		list.setBackground(Color.LIGHT_GRAY);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse)
			{
				if(!lse.getValueIsAdjusting())
				{
					int selectedIndex = list.getSelectedIndex();										
					if(selectedIndex < 0)
						return;
					AutoCompleteItem item = model.getItem(selectedIndex);
					if(item != null)
						propertiesWindow.showProperties(item);					
				}
			}
		});
		
		this.setPreferredSize(new Dimension(200, 200));
		this.add(jScrollPane);
		this.doLayout();
		pack();
	}

	private void addListenersToTextComponent()
	{
		KeyAdapter keyAdapter = new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.isConsumed()) return;
				if (e.getKeyCode() == KeyEvent.VK_SPACE && (e.isControlDown() || e.isShiftDown()))
				{						
					if(e.isShiftDown() && e.isControlDown())
						searchStrategy = new LinearContainsSearch();
					else
						searchStrategy = new BinarySearch();
				
					
					addSubSuggestions();
					updateAutoComplete();
					if(model.getSize()==1)
						insertSelectedItem();
					e.consume();
				}
				

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
				if (AutoCompletePopup.this.isVisible())
				{
					if (e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z)
					{
						updateAutoComplete();
						e.consume();
					}
				}
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
		String[] words = subSuggestionsWordSearchProvider.getWordsToSearchForSubSuggestions(textComponent);
		Arrays.sort(words);
		
		int j = 0;
		for (int i = 0; i < words.length && j < items.size(); i++)
		{
			String curWord = words[i];	
			AutoCompleteItem item = items.get(j);
			while (item.getAutoCompleteId().toLowerCase().compareTo(words[i]) <= 0 && j < items.size()-1)
			{				
				int compare = item.getAutoCompleteId().toLowerCase().compareTo(curWord);
				if (compare == 0)
					subSuggestions.addAll(items.get(j).getSubSuggestions());
				j++;
				item = items.get(j);
			}			
		}
	}

	public void setAutoCompletePossibilties(List<? extends AutoCompleteItem> items)
	{
		this.items = new ArrayList<AutoCompleteItem>(items);		
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
		currentWordBounds = TextEditorUtils.getWordBounds(textComponent, TextEditorUtils.ExpansionDirection.LEFT);
		String wordPart = TextEditorUtils.getCurrentWord(currentWordBounds, textComponent);
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
		Integer wordStartIndex = currentWordBounds.getFirst();
		Integer wordEndIndex = currentWordBounds.getSecond();
		AutoCompleteItem item = (AutoCompleteItem)list.getSelectedValue();
		String autoCompletion = item.getAutoCompletion();
		
		textComponent.setSelectionStart(wordStartIndex);
		textComponent.setSelectionEnd(wordEndIndex);
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
