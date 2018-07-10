package com.betadb.gui.connection;

import com.betadb.gui.dao.DbInfoDAO;
import com.betadb.gui.dbobjects.Parameter;
import com.betadb.gui.dbobjects.Procedure;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author parmstrong
 */
public class LazyLoadParametersNode extends LazyLoadNode
{
    private final DbInfoDAO dbInfoDAO;
    private final Procedure procedure;

    public LazyLoadParametersNode(DbInfoDAO dbInfoDAO,  Procedure procedure, DefaultTreeModel treeModel)
    {
        super("Parameters", treeModel);
        this.dbInfoDAO = dbInfoDAO;
        this.procedure = procedure;

    }

    @Override
    public void performLoadAction()
    {
        List<Parameter> parameters = procedure.getParameters();
        for (int i = 0; i < parameters.size(); i++)
        {
            Parameter parameter = parameters.get(i);
            DefaultMutableTreeNode indexNode = new DefaultMutableTreeNode(parameter);
            treeModel.insertNodeInto(indexNode, this, i);
        }

        treeModel.removeNodeFromParent(loadingNode);
    }
}
