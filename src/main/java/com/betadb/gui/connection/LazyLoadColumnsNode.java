package com.betadb.gui.connection;

import com.betadb.gui.dao.DbInfoDAO;
import com.betadb.gui.dbobjects.Column;
import com.betadb.gui.dbobjects.Table;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author parmstrong
 */
public class LazyLoadColumnsNode extends LazyLoadNode
{
    private final DbInfoDAO dbInfoDAO;
    private final String dbName;
    private final Table table;

    public LazyLoadColumnsNode(DbInfoDAO dbInfoDAO, String dbName, Table table, DefaultTreeModel treeModel)
    {
        super("Columns", treeModel);
        this.dbInfoDAO = dbInfoDAO;
        this.dbName = dbName;
        this.table = table;

    }

    @Override
    public void performLoadAction()
    {
        List<Column> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++)
        {
            Column column = columns.get(i);
            DefaultMutableTreeNode indexNode = new DefaultMutableTreeNode(column);
            treeModel.insertNodeInto(indexNode, this, i);
        }

        treeModel.removeNodeFromParent(loadingNode);
    }
}
