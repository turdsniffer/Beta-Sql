package com.betadb.gui.connection;

import com.betadb.gui.dbobjects.PrimaryKey;
import com.betadb.gui.dbobjects.Table;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author parmstrong
 */
public class LazyLoadPrimaryKeyNode extends LazyLoadNode
{
    private Table table;

    public LazyLoadPrimaryKeyNode(Table table, DefaultTreeModel treeModel, JTree treeDbs)
    {
        super("Primary Keys", treeModel, treeDbs);
        this.table = table;
    }

    @Override
    protected void performLoadAction()
    {

        List<PrimaryKey> primaryKeys = table.getPrimaryKeys();
        for (int i = 0; i < primaryKeys.size(); i++)
        {
            PrimaryKey primaryKey = primaryKeys.get(i);
            DefaultMutableTreeNode foreignKeyNode = new DefaultMutableTreeNode(primaryKey);
            treeModel.insertNodeInto(foreignKeyNode, this, i);
        }

        treeModel.removeNodeFromParent(loadingNode);
    }

}
