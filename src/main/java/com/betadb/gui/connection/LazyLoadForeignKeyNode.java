package com.betadb.gui.connection;

import com.betadb.gui.dao.DbInfoDAO;
import com.betadb.gui.dbobjects.ForeignKey;
import com.betadb.gui.dbobjects.Table;
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
