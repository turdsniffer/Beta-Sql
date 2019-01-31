package com.betadb.gui.connection;

import com.betadb.gui.dbobjects.Index;
import com.betadb.gui.dbobjects.Table;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author parmstrong
 */
public class LazyLoadIndexNode extends LazyLoadNode
{

    private final Table table;

    public LazyLoadIndexNode(Table table, DefaultTreeModel treeModel, JTree treeDbs)
    {
        super("Indexes",  treeModel, treeDbs);
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
