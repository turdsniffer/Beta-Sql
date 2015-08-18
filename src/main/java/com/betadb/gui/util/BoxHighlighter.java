/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.betadb.gui.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

/**
 *
 * @author parmstrong
 */
public class BoxHighlighter extends DefaultHighlighter.DefaultHighlightPainter
{

    public BoxHighlighter(Color c)
    {
        super(c);
    }

    public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c)
    {
        Rectangle alloc = bounds.getBounds();
        try
        {
            // --- determine locations ---
            TextUI mapper = c.getUI();
            Rectangle p0 = mapper.modelToView(c, offs0);
            Rectangle p1 = mapper.modelToView(c, offs1);

            // --- render ---
            Color color = getColor();

            if (color == null)
            {
                g.setColor(c.getSelectionColor());
            }
            else
            {
                g.setColor(color);
            }
            if (p0.y == p1.y)
            {
                // same line, render a rectangle
                Rectangle r = p0.union(p1);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
            else
            {
                // different lines
                int p0ToMarginWidth = alloc.x + alloc.width - p0.x;
                g.fillRect(p0.x, p0.y, p0ToMarginWidth, p0.height);
                if ((p0.y + p0.height) != p1.y)
                {
                    g.fillRect(alloc.x, p0.y + p0.height, alloc.width,
                               p1.y - (p0.y + p0.height));
                }
                g.fillRect(alloc.x, p1.y, (p1.x - alloc.x), p1.height);
            }
        }
        catch (BadLocationException e)
        {
            // can't render
        }
    }

    // --- LayerPainter methods ----------------------------
    /**
     * Paints a portion of a highlight.
     *
     * @param g the graphics context
     * @param offs0 the starting model offset &gt;= 0
     * @param offs1 the ending model offset &gt;= offs1
     * @param bounds the bounding box of the view, which is not necessarily the
     * region to paint.
     * @param c the editor
     * @param view View painting for
     * @return region drawing occurred in
     */
    public Shape paintLayer(Graphics g, int offs0, int offs1,
                            Shape bounds, JTextComponent c, View view)
    {
        Color color = getColor();

        if (color == null)
        {
            g.setColor(c.getSelectionColor());
        }
        else
        {
            g.setColor(color);
        }

        Rectangle r;

        if (offs0 == view.getStartOffset()
            && offs1 == view.getEndOffset())
        {
            // Contained in view, can just use bounds.
            if (bounds instanceof Rectangle)
            {
                r = (Rectangle) bounds;
            }
            else
            {
                r = bounds.getBounds();
            }
        }
        else
        {
            // Should only render part of View.
            try
            {
                // --- determine locations ---
                Shape shape = view.modelToView(offs0, Position.Bias.Forward,
                                               offs1, Position.Bias.Backward,
                                               bounds);
                r = (shape instanceof Rectangle)
                    ? (Rectangle) shape : shape.getBounds();
            }
            catch (BadLocationException e)
            {
                // can't render
                r = null;
            }
        }

        if (r != null)
        {
                // If we are asked to highlight, we should draw something even
            // if the model-to-view projection is of zero width (6340106).
            r.width = Math.max(r.width, 1);

            g.drawRect(r.x, r.y, r.width-1, r.height -1);
        }

        return r;
    }

    private Color color;

}
