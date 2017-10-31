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
import com.betadb.gui.dbobjects.DbObject;
import com.betadb.gui.util.TemplateEditor;
import com.betadb.gui.util.UnderlineHighlighter;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.swingautocompletion.util.Pair;
import com.swingautocompletion.util.TextEditorUtils;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import sqltemplate.SqlTemplateDialog;

/**
 *
 * @author parmstrong
 */
public class EditorPanel extends javax.swing.JPanel implements EventListener, ActionListener
{

    private final RSyntaxTextArea codeEditor;
    private final AutoCompletePopup autoCompletePopup;
    private DbInfo dbInfo;
    boolean isTemplateEditing = false;
    private final TemplateEditor templateEditor;
    private final EventManager EventManager;
    private Map<String, DbObject> autoCompleteToDbObjectMap = Maps.newHashMap();
    private UnderlineHighlighter navigationHightlight = new UnderlineHighlighter(Color.blue);
    private Object navigationHighlight;
    private SqlTemplateDialog sqlTemplateDialog;

    /**
     * Creates new form EditorPanel
     *
     * @param connectionInfo
     */
    @Inject
    public EditorPanel(EventManager eventManager, SqlTemplateDialog sqlTemplateDialog)
    {
        eventManager.addEventListener(this);
        this.EventManager = eventManager;
		this.sqlTemplateDialog = sqlTemplateDialog;
        initComponents();

        codeEditor = new RSyntaxTextArea();
        codeEditor.setCodeFoldingEnabled(true);
        KeyAdapter keyAdapter = new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.isConsumed())
                {
                    return;
                }
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F)
                {
                    searchBar.setVisible(true);     
                    txtSearchField.requestFocus();
					txtSearchField.selectAll();
                }
            }
        };
		codeEditor.addKeyListener(keyAdapter);
        templateEditor = new TemplateEditor(codeEditor);
        txtSearchField.addKeyListener(keyAdapter);
        searchBar.setVisible(false);
        SqlSubSuggestionsWordSearchProvider sqlSubSuggestionsWordSearchProvider = new SqlSubSuggestionsWordSearchProvider();
        autoCompletePopup = new AutoCompletePopup(codeEditor, new BetaDbPopupListCellRenderer(), sqlSubSuggestionsWordSearchProvider, sqlSubSuggestionsWordSearchProvider);
        autoCompletePopup.addAutoCompleteHandler((AutoCompleteItem autoCompleteItem) ->
        {
            int startPosition = codeEditor.getCaretPosition() - autoCompleteItem.getAutoCompletion().length();
            templateEditor.initiateTemplateEditing(startPosition, autoCompleteItem.getAutoCompletion().length());
        });

        JScrollPane scrPane = new JScrollPane(codeEditor);
        this.add(scrPane);
        this.doLayout();

        codeEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        codeEditor.addCaretListener(new WordHighlighter());//Highlight occurances of current word.
        codeEditor.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseMoved(MouseEvent e)
            {      
                if(navigationHighlight != null)
                {
                    codeEditor.getHighlighter().removeHighlight(navigationHighlight);
                    navigationHighlight = null;
                }
                
                if(e.isControlDown())
                {
                    int characterPosition = codeEditor.viewToModel(e.getPoint());
                    Pair<Integer, Integer> wordBounds = TextEditorUtils.getWordBounds(codeEditor, new TextEditorUtils.WordBoundsConfig().withExpansionDirection(TextEditorUtils.ExpansionDirection.BOTH).withStartingPosition(characterPosition));
                    String word = TextEditorUtils.getCurrentWord(wordBounds, codeEditor);
                    DbObject dbObject = autoCompleteToDbObjectMap.get(word);
                    if(dbObject != null)
                    {
                        try
                        {
                            navigationHighlight = codeEditor.getHighlighter().addHighlight(wordBounds.getFirst(), wordBounds.getSecond(), navigationHightlight);                            
                        }
                        catch (BadLocationException ex)
                        {
                            Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        codeEditor.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e) 
            {               
                if(e.isControlDown())
                {
                    int characterPosition = codeEditor.viewToModel(e.getPoint());
                    String word = TextEditorUtils.getWord(codeEditor, new TextEditorUtils.WordBoundsConfig().withExpansionDirection(TextEditorUtils.ExpansionDirection.BOTH).withStartingPosition(characterPosition));
                    DbObject dbObject = autoCompleteToDbObjectMap.get(word);
                    EventManager.fireEvent(Event.DB_OBJECT_SELECTED, dbObject);
                }
            }
        });
		
		codeEditor.setPopupMenu(jPopupMenu1);

    }
    


    public void setDbConnectInfo(DbConnection connectionInfo)
    {
        dbInfo = connectionInfo.getDbInfo();
        codeEditor.setText(connectionInfo.getStartingSql());
        refreshAutoCompleteOptions();
    }

    private void refreshAutoCompleteOptions()
    {
        List<AutoCompleteItem> autoCompletePossibilities = new ArrayList<>();
        autoCompletePossibilities.addAll(DefaultAutoCompleteItems.getitems());
        autoCompletePossibilities.addAll(sqlTemplateDialog.getSavedTemplatesAsAutoCompleteItems());
        autoCompletePossibilities.addAll(dbInfo.getAllDbObjects());
		autoCompletePopup.setAutoCompletePossibilties(autoCompletePossibilities);
		

        for (DbObject dbObject : dbInfo.getAllDbObjects())
        {
            autoCompleteToDbObjectMap.put(dbObject.getAutoCompleteId(), dbObject);
            for (String alternateAutoComplete : dbObject.alternateAutoCompeteIds())
                autoCompleteToDbObjectMap.put(alternateAutoComplete, dbObject);
        }

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
        {
            return codeEditor.getText();
        }

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
    private void initComponents()
    {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        btnEditTemplate = new javax.swing.JMenuItem();
        searchBar = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        txtSearchField = new javax.swing.JTextField();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnMatchCase = new javax.swing.JToggleButton();
        btnRegex = new javax.swing.JToggleButton();

        btnEditTemplate.setText("Create/Edit Template");
        btnEditTemplate.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditTemplateActionPerformed(evt);
            }
        });
        jPopupMenu1.add(btnEditTemplate);

        setPreferredSize(new java.awt.Dimension(0, 300));
        setLayout(new java.awt.BorderLayout());

        searchBar.setRollover(true);

        jLabel1.setText("Find:");
        searchBar.add(jLabel1);

        txtSearchField.setMinimumSize(new java.awt.Dimension(200, 19));
        txtSearchField.setPreferredSize(new java.awt.Dimension(200, 19));
        txtSearchField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtSearchFieldActionPerformed(evt);
            }
        });
        searchBar.add(txtSearchField);

        btnNext.setText("Next");
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNext.setActionCommand("FindNext");
        btnNext.addActionListener(this);
        searchBar.add(btnNext);

        btnPrev.setText("Prev");
        btnPrev.setFocusable(false);
        btnPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrev.setActionCommand("FindPrev");
        btnPrev.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrev.addActionListener(this);
        searchBar.add(btnPrev);

        btnMatchCase.setText("Match Case");
        btnMatchCase.setFocusable(false);
        btnMatchCase.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMatchCase.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        searchBar.add(btnMatchCase);

        btnRegex.setText("Regex");
        btnRegex.setFocusable(false);
        btnRegex.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRegex.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        searchBar.add(btnRegex);

        add(searchBar, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtSearchFieldActionPerformed
    {//GEN-HEADEREND:event_txtSearchFieldActionPerformed
        btnNext.doClick(0);
    }//GEN-LAST:event_txtSearchFieldActionPerformed

    private void btnEditTemplateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditTemplateActionPerformed
    {//GEN-HEADEREND:event_btnEditTemplateActionPerformed
		String selectedText = codeEditor.getSelectedText();
		sqlTemplateDialog.createAsTemplate(selectedText);
    }//GEN-LAST:event_btnEditTemplateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem btnEditTemplate;
    private javax.swing.JToggleButton btnMatchCase;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JToggleButton btnRegex;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JToolBar searchBar;
    private javax.swing.JTextField txtSearchField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void EventOccurred(Event event, Object value)
    {
        if (event.equals(Event.DB_INFO_UPDATED))
        {
            DbInfo dbInfo = (DbInfo) value;
            if (this.dbInfo.equals(dbInfo))
            {
                refreshAutoCompleteOptions();
            }
        }
    }

    String getCurrentWord()
    {
        return TextEditorUtils.getCurrentWord(codeEditor, new TextEditorUtils.WordBoundsConfig().withWordSeparators(Lists.newArrayList('\t', '\n', ' ')));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // "FindNext" => search forward, "FindPrev" => search backward
        String command = e.getActionCommand();
        boolean forward = "FindNext".equals(command);

        // Create an object defining our search parameters.
        SearchContext context = new SearchContext();
        String text = txtSearchField.getText();
        if (text.length() == 0)
        {
            return;
        }

        context.setSearchFor(text);
        context.setMatchCase(btnMatchCase.isSelected());
        context.setRegularExpression(btnRegex.isSelected());
        context.setSearchForward(forward);
        context.setWholeWord(false);

        boolean found = SearchEngine.find(codeEditor, context);
        if (!found)
        {
            JOptionPane.showMessageDialog(this, "Text not found");
        }

    }

    void linkItemAtCaretToObjectTree()
    {
        String sql = this.getCurrentWord();
        List<DbObject> matchingDbObjects = dbInfo.getAllDbObjects().stream().filter(o -> o.getAutoCompleteId().equalsIgnoreCase(sql) || o.alternateAutoCompeteIds().contains(sql)).collect(Collectors.toList());
        if (matchingDbObjects.size() == 1)
            EventManager.fireEvent(Event.DB_OBJECT_SELECTED, matchingDbObjects.get(0));
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
