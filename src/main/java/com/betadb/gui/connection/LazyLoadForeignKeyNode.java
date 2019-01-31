package com.betadb.gui.connection;

import com.betadb.gui.dbobjects.ForeignKey;
import com.betadb.gui.dbobjects.Table;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author parmstrong
 */
public class LazyLoadForeignKeyNode extends LazyLoadNode
{

    private Table table;

    public LazyLoadForeignKeyNode(Table table, DefaultTreeModel treeModel, JTree treeDbs)
    {
        super("Foreign Keys",  treeModel, treeDbs);
        this.table = table;
    }

    @Override
    protected void performLoadAction()
    {

        List<ForeignKey> foreignKeys = table.getForeignKeys();

        for (int i = 0; i < foreignKeys.size(); i++)
        {
            ForeignKey foreignKey = foreignKeys.get(i);
            DefaultMutableTreeNode foreignKeyNode = new DefaultMutableTreeNode(foreignKey);
            treeModel.insertNodeInto(foreignKeyNode, this, i);
        }

        treeModel.removeNodeFromParent(loadingNode);
    }

}
