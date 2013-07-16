
package com.betadb.gui.table.util.renderer;

import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * @author parmstrong
 */
public class RendererUtils
{
	public static void formatColumns(JTable table, TableCellRenderer renderer)
	{
		Enumeration<TableColumn> columns = table.getColumnModel().getColumns();

		while (columns.hasMoreElements())
		{
			TableColumn column = columns.nextElement();
			column.setCellRenderer(renderer);
		}
	}
}
