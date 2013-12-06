
package com.betadb.gui.connection;

import com.betadb.gui.dao.DbInfoDAO;
import com.betadb.gui.dbobjects.ForeignKey;
import com.betadb.gui.dbobjects.PrimaryKey;
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
public class LazyLoadPrimaryKeyNode extends LazyLoadNode
{
	private DbInfoDAO dbInfoDAO;
	private String dbName;
	private Table table;

	public LazyLoadPrimaryKeyNode(DataSource datasource, String dbName, Table table, DefaultTreeModel treeModel)
	{
		super("Primary Keys", datasource, treeModel);
		dbInfoDAO = new DbInfoDAO(datasource);
		this.dbName = dbName;
		this.table = table;
	}

	@Override
	protected void performLoadAction()
	{
		try
		{
			List<PrimaryKey> primaryKeys = dbInfoDAO.getPrimaryKeys(dbName, table);
			table.setPrimaryKeys(primaryKeys);
			for (int i = 0; i < primaryKeys.size(); i++)
			{
				PrimaryKey primaryKey = primaryKeys.get(i);
				DefaultMutableTreeNode foreignKeyNode = new DefaultMutableTreeNode(primaryKey);
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
