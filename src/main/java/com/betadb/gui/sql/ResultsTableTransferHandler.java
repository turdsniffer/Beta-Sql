package com.betadb.gui.sql;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 * @author parmstrong
 */
public class ResultsTableTransferHandler extends TransferHandler
{
	@Override
	public int getSourceActions(JComponent c)
	{
		return COPY;
	}

	@Override
	protected Transferable createTransferable(JComponent c)
	{
		JTable table = (JTable) c;
		int[] rows = table.getSelectedRows();
		int[] cols = table.getSelectedColumns();

		if(rows.length == 1 && cols.length == 1)
			return new StringSelection(table.getValueAt(rows[0], cols[0]).toString());
		
		StringBuilder textBuff = new StringBuilder();

		String columnSeparator = cols.length > 1 ? "\t":"";
		
		for (int row = 0; row < rows.length; row++)
		{			
			for (int col = 0; col < cols.length; col++)
			{
				Object obj = table.getValueAt(rows[row], cols[col]);
				String val = ((obj == null) ? "" : obj.toString());

				textBuff.append( val + columnSeparator);
			}
			// we want a newline at the end of each line and not a tab			
			textBuff.append("\n");
		}
		return new StringSelection(textBuff.toString());
	}
}
