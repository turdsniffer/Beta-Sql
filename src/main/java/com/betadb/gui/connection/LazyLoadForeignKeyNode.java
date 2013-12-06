
package com.betadb.gui.connection;

import com.betadb.gui.dao.DbInfoDAO;
import com.betadb.gui.dbobjects.ForeignKey;
import com.betadb.gui.dbobjects.Table;
import com.betadb.gui.exception.BetaDbException;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author parmstrong
 */
public class LazyLoadForeignKeyNode extends LazyLoadNode
{
	private DbInfoDAO dbInfoDAO;
	private String dbName;
	private Table table;

	public LazyLoadForeignKeyNode(DataSource datasource, String dbName, Table table, DefaultTreeModel treeModel)
	{
		super("Foreign Keys", datasource, treeModel);
		dbInfoDAO = new DbInfoDAO(datasource);
		this.dbName = dbName;
		this.table = table;
	}

	@Override
	protected void performLoadAction()
	{
		try
		{
			List<ForeignKey> foreignKeys = dbInfoDAO.getForeignKeys(dbName, table);
			table.setForeignKeys(foreignKeys);
			for (int i = 0; i < foreignKeys.size(); i++)
			{
				ForeignKey foreignKey = foreignKeys.get(i);
				DefaultMutableTreeNode foreignKeyNode = new DefaultMutableTreeNode(foreignKey);
				treeModel.insertNodeInto(foreignKeyNode, this, i);
			}
		}
		catch (SQLException ex)
		{
			throw new BetaDbException("Error Loading Index data", ex);
		}
		treeModel.removeNodeFromParent(loadingNode);
	}

}
