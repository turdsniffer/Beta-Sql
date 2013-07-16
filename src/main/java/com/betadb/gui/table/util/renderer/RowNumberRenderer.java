
package com.betadb.gui.table.util.renderer;

import java.awt.Component;
import javax.swing.JButton;


/**
 * @author parmstrong
 */
public class RowNumberRenderer implements CellClassRenderer<Integer>
{
	JButton button = new JButton();

	public RowNumberRenderer()
	{
		button.setBorder(javax.swing.BorderFactory.createEmptyBorder());		
	}

	@Override
	public Component render(Integer t, Component c)
	{
		button.setText(Integer.toString(t));
		return button;
	}
}
