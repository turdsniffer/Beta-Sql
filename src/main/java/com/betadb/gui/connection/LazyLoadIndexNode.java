
package com.betadb.gui.connection;

import com.betadb.gui.dao.DbInfoDAO;
import com.betadb.gui.dbobjects.Index;
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
public class LazyLoadIndexNode extends LazyLoadNode
{
	private DbInfoDAO dbInfoDAO;
	private String dbName;
	private Table table;

	
	public LazyLoadIndexNode(DataSource datasource, String dbName, Table table, DefaultTreeModel treeModel)
	{
		super("Indexes", datasource, treeModel);
		dbInfoDAO = new DbInfoDAO(datasource);
		this.dbName = dbName;
		this.table = table;

	}

	@Override
	public void performLoadAction()
	{
		try
		{
			List<Index> indexes = dbInfoDAO.getIndexes(dbName, table);
			table.setIndexes(indexes);		
			for (int i = 0; i < indexes.size(); i++)
			{
				Index index = indexes.get(i);
				DefaultMutableTreeNode indexNode = new DefaultMutableTreeNode(index);
				treeModel.insertNodeInto(indexNode, this, i);
			}
		}
		catch (SQLException ex)
		{
			throw new BetaDbException("Error Loading Index data", ex);
		}
		treeModel.removeNodeFromParent(loadingNode);
	}
}
