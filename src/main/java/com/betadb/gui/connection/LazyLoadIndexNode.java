package com.betadb.gui.connection;

import com.betadb.gui.dao.DbInfoDAO;
import com.betadb.gui.dbobjects.Index;
import com.betadb.gui.dbobjects.Table;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author parmstrong
 */
public class LazyLoadIndexNode extends LazyLoadNode
{
    private final DbInfoDAO dbInfoDAO;
    private final String dbName;
    private final Table table;

    public LazyLoadIndexNode(DbInfoDAO dbInfoDAO, String dbName, Table table, DefaultTreeModel treeModel)
    {
        super("Indexes",  treeModel);
        this.dbInfoDAO = dbInfoDAO;
        this.dbName = dbName;
        this.table = table;

    }

    @Override
    public void performLoadAction()
    {

        List<Index> indexes = table.getIndexes();
        for (int i = 0; i < indexes.size(); i++)
        {
            Index index = indexes.get(i);
            DefaultMutableTreeNode indexNode = new DefaultMutableTreeNode(index);
            treeModel.insertNodeInto(indexNode, this, i);
        }

        treeModel.removeNodeFromParent(loadingNode);
    }
}
