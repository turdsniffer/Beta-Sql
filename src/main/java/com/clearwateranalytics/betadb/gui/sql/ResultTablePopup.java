package com.clearwateranalytics.betadb.gui.sql;

import com.clearwateranalytics.betadb.gui.cellviewer.CellViewer;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 * @author parmstrong
 */
public class ResultTablePopup extends JPopupMenu
{
	public ResultTablePopup()
	{
		super();
		JMenuItem btnCopyIn = new JMenuItem("Copy as SQL IN statement");
		btnCopyIn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				JTable table = (JTable) getInvoker();
				int rowIndexStart = table.getSelectedRow();
				int rowIndexEnd = table.getSelectionModel().getMaxSelectionIndex();
				int colIndexStart = table.getSelectedColumn();
				int colIndexEnd = table.getColumnModel().getSelectionModel().getMaxSelectionIndex();

				StringBuilder selectedText = new StringBuilder();
				for (int c = colIndexStart; c <= colIndexEnd; c++)
				{
					selectedText.append("(");
					for (int r = rowIndexStart; r <= rowIndexEnd; r++)
					{
						if (table.isCellSelected(r, c))
						{
							selectedText.append(getQueryRepresentation(table.getModel().getValueAt(table.convertRowIndexToModel(r), table.convertColumnIndexToModel(c))) + ",");
						}
					}
					selectedText.deleteCharAt(selectedText.length() - 1);
					selectedText.append(")\n");
				}

				StringSelection ss = new StringSelection(selectedText.toString());
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			}
		});
		this.add(btnCopyIn);
		
			JMenuItem btnInsert = new JMenuItem("Create SQL Insert Statement");
		btnInsert.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				JTable table = (JTable) getInvoker();
				int rowIndexStart = table.getSelectedRow();
				int rowIndexEnd = table.getSelectionModel().getMaxSelectionIndex();
				int colIndexStart = table.getSelectedColumn();
				int colIndexEnd = table.getColumnModel().getSelectionModel().getMaxSelectionIndex();
				ResultsTableModel resultsTableModel = (ResultsTableModel)table.getModel();
				
				StringBuilder selectedText = new StringBuilder();
				for (int r = rowIndexStart; r <= rowIndexEnd; r++)
				{
					selectedText.append("insert into values(");
					for (int c = colIndexStart; c <= colIndexEnd; c++)
					{
						if (table.isCellSelected(r, c))
						{							
							selectedText.append(getQueryRepresentation(table.getModel().getValueAt(table.convertRowIndexToModel(r), table.convertColumnIndexToModel(c))) + ",");
						}
					}
					selectedText.deleteCharAt(selectedText.length() - 1);
					selectedText.append(")\n");
				}

				StringSelection ss = new StringSelection(selectedText.toString());
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			}
		});
		this.add(btnInsert);

		JMenuItem btnTearOut = new JMenuItem("Show in new window");
		btnTearOut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				JTable table = (JTable) getInvoker();
				TearOutDialog tearOutDialog = new TearOutDialog(table);
				tearOutDialog.setVisible(true);
			}
		});
		this.add(btnTearOut);

		JMenuItem btnViewer = new JMenuItem("Cell detail view");
		btnViewer.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				JTable table = (JTable) getInvoker();
				int selectedColumn = table.getSelectedColumn();
				int selectedRow = table.getSelectedRow();
				Class columnClass = table.getColumnClass(selectedColumn);
				Object cellValue = table.getValueAt(selectedRow, selectedColumn);
				CellViewer.getInstance().setValue(columnClass, cellValue);
			}
		});
		this.add(btnViewer);


	

	}

	private static String getQueryRepresentation(Object obj)
	{
		if (obj instanceof String)
			return "'" + obj.toString() + "'";
		if (obj instanceof Boolean)
			return ((Boolean) obj) ? "1" : "0";
		if(obj instanceof Date)
			return "'"+obj.toString()+"'";
		if(obj == null)
			return "null";
		
		return obj.toString();
	}
}
