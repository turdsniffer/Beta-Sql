/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditorPanel.java
 *
 * Created on Jun 21, 2011, 2:35:12 PM
 */
package com.betadb.gui.sql;

import com.betadb.gui.autocomplete.BetaDbPopupListCellRenderer;
import com.betadb.gui.autocomplete.SqlSubSuggestionsWordSearchProvider;
import com.betadb.gui.connection.DbConnection;
import com.betadb.gui.dbobjects.DbInfo;
import com.betadb.gui.events.Event;
import com.betadb.gui.events.EventListener;
import com.betadb.gui.events.EventManager;
import com.google.common.collect.Lists;
import com.swingautocompletion.main.AutoCompleteItem;
import com.swingautocompletion.main.AutoCompletePopup;
import com.betadb.gui.autocomplete.DefaultAutoCompleteItems;
import com.swingautocompletion.util.TextEditorUtils;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;


/**
 *
 * @author parmstrong
 */
public class EditorPanel extends javax.swing.JPanel implements EventListener
{

	final RSyntaxTextArea codeEditor;
	final AutoCompletePopup autoCompletePopup;
	final DbInfo dbInfo;

	/**
	 * Creates new form EditorPanel
	 */
	public EditorPanel(DbConnection connectionInfo)
	{
		EventManager.getInstance().addEventListener(this);
		dbInfo = connectionInfo.getDbInfo();
		initComponents();
		codeEditor = new RSyntaxTextArea();
		SqlSubSuggestionsWordSearchProvider sqlSubSuggestionsWordSearchProvider = new SqlSubSuggestionsWordSearchProvider();
		autoCompletePopup = new AutoCompletePopup(codeEditor, new BetaDbPopupListCellRenderer(), sqlSubSuggestionsWordSearchProvider, sqlSubSuggestionsWordSearchProvider);
		refreshAutoCompleteOptions();
		JScrollPane scrPane = new JScrollPane(codeEditor);
		this.add(scrPane);
		this.doLayout();

		codeEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		codeEditor.setText(connectionInfo.getStartingSql());
		codeEditor.addCaretListener(new WordHighlighter());//Highlight occurances of current word.
	}

	private void refreshAutoCompleteOptions()
	{
		List<AutoCompleteItem> autoCompletePossibilities = new ArrayList<>();
		autoCompletePossibilities.addAll(dbInfo.getAllDbObjects());
		autoCompletePossibilities.addAll(DefaultAutoCompleteItems.getitems());
		autoCompletePopup.setAutoCompletePossibilties(autoCompletePossibilities);
	}
	
	public void setSql(String sql)
	{
		codeEditor.setText(sql);
	}

	public void appendSql(String sql)
	{
		String text = codeEditor.getText();
		codeEditor.setText(text + "\n" + sql);
	}

	public String getSql(boolean all)
	{
		if (all)
			return codeEditor.getText();

		String selectedText = codeEditor.getSelectedText();
		selectedText = selectedText == null ? TextEditorUtils.getCurrentTextBlock(codeEditor) : selectedText;
		return selectedText == null || selectedText.trim().isEmpty() ? codeEditor.getText() : selectedText;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(0, 300));
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

	@Override
	public void EventOccurred(Event event, Object value)
	{
		if (event.equals(Event.DB_INFO_UPDATED))
		{
			DbInfo dbInfo = (DbInfo) value;
			if (this.dbInfo.equals(dbInfo))
				refreshAutoCompleteOptions();
		}
	}

	String getCurrentWord()
	{
		return TextEditorUtils.getCurrentWord(codeEditor,Lists.newArrayList('\t','\n',' '));
	}

	private class WordHighlighter implements CaretListener
	{

		@Override
		public void caretUpdate(CaretEvent ce)
		{
			String currentWord = getCurrentWord();
			TextEditorUtils.highlightWord(codeEditor, currentWord);
		}
	}
}
