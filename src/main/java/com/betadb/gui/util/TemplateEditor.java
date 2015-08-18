/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.betadb.gui.util;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author parmstrong
 */
public class TemplateEditor
{

    private JTextComponent textComponent;
    private Highlighter.HighlightPainter boxHighlighter = new BoxHighlighter(Color.RED);

    public TemplateEditor(JTextComponent textComponent)
    {
        this.textComponent = textComponent;
    }

    public void initiateTemplateEditing(int templateStartPosition, int templateLength)
    {
        try
        {
            removeTemplateHighlights(textComponent);
            Highlighter highlighter = textComponent.getHighlighter();

            String text = textComponent.getDocument().getText(templateStartPosition, templateLength);
            int paramIndex = text.indexOf('?');

            if (paramIndex > 0)
            {
                addTemplateEventListeners(textComponent);
            }
            while (paramIndex > 0)
            {
                highlighter.addHighlight(templateStartPosition + paramIndex, templateStartPosition + paramIndex + 1, boxHighlighter);
                paramIndex = text.indexOf('?', paramIndex + 1);
            }
            moveToNextTag();
        }
        catch (BadLocationException ex)
        {
            Logger.getLogger(TemplateEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void exitTemplateEditing()
    {
        removeTemplateHighlights(textComponent);
        textComponent.removeKeyListener(templateKeyAdapter);
        textComponent.getDocument().removeDocumentListener(templateDocumentListener);
    }

    private void addTemplateEventListeners(JTextComponent textComponent)
    {
        textComponent.addKeyListener(templateKeyAdapter);
        textComponent.getDocument().addDocumentListener(templateDocumentListener);
    }

    private KeyAdapter templateKeyAdapter = new KeyAdapter()
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            if (e.isConsumed())
            {
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            {
                exitTemplateEditing();
                e.consume();
            }

            if (e.getKeyCode() == KeyEvent.VK_TAB)
            {

                moveToNextTag();
                e.consume();
            }

        }

    };

    private void removeTemplateHighlights(JTextComponent textComponent)
    {
        Highlighter hilite = textComponent.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i = 0; i < hilites.length; i++)
        {
            if (hilites[i].getPainter() instanceof BoxHighlighter)
            {
                hilite.removeHighlight(hilites[i]);
            }
        }

    }

    private DocumentListener templateDocumentListener = new DocumentListener()
    {
        @Override
        public void insertUpdate(DocumentEvent e)
        {
            removeRelatedHighlight(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
            removeRelatedHighlight(e);
        }

        private void removeRelatedHighlight(DocumentEvent e)
        {
            int offset = e.getOffset();
            Highlighter hilite = textComponent.getHighlighter();
            Highlighter.Highlight[] hilites = hilite.getHighlights();

            for (Highlighter.Highlight curHilite : hilites)
                if (curHilite.getPainter() instanceof BoxHighlighter && curHilite.getStartOffset() <= offset && curHilite.getEndOffset() >= offset)
                    hilite.removeHighlight(curHilite);
            checkForFinishedEditing();
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {
        }
    };
    
    private void checkForFinishedEditing()
    {   
        Highlighter hilite = textComponent.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();
        for (Highlighter.Highlight curHilite : hilites)
            if (curHilite.getPainter() instanceof BoxHighlighter)
                return;
        exitTemplateEditing();
    }

    private void moveToNextTag()
    {
        Highlighter highlighter = textComponent.getHighlighter();

        int caretPosition = textComponent.getCaretPosition();
        Highlighter.Highlight currentHighlight = null;
        TreeMap<Integer, Highlighter.Highlight> highlightMap = new TreeMap<>();
        for (Highlighter.Highlight hilite : highlighter.getHighlights())
            if (hilite.getPainter() instanceof BoxHighlighter)
                highlightMap.put(hilite.getStartOffset(), hilite);

        if (highlightMap.isEmpty())
            return;
        Map.Entry<Integer, Highlighter.Highlight> floorEntry = highlightMap.floorEntry(caretPosition);
        currentHighlight = floorEntry == null ? null : highlightMap.floorEntry(caretPosition).getValue();

        Highlighter.Highlight nextHighlight = currentHighlight == null || highlightMap.higherEntry(currentHighlight.getEndOffset()) == null ? highlightMap.firstEntry().getValue() : highlightMap.higherEntry(currentHighlight.getEndOffset()).getValue();

        textComponent.setCaretPosition(nextHighlight.getStartOffset());
        textComponent.setSelectionStart(nextHighlight.getStartOffset());
        textComponent.setSelectionEnd(nextHighlight.getEndOffset());
    }

}
