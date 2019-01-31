package com.betadb.gui.connection;

import com.betadb.gui.dbobjects.Parameter;
import com.betadb.gui.dbobjects.Procedure;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author parmstrong
 */
public class LazyLoadParametersNode extends LazyLoadNode
{
    private final Procedure procedure;

    public LazyLoadParametersNode(  Procedure procedure, DefaultTreeModel treeModel, JTree treeDbs)
    {
        super("Parameters", treeModel, treeDbs);
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
