package com.betadb.gui.connection;

import com.betadb.gui.dbobjects.Function;
import com.betadb.gui.dbobjects.Parameter;
import com.betadb.gui.dbobjects.Procedure;
import com.betadb.gui.dbobjects.Schema;
import com.betadb.gui.dbobjects.Table;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author parmstrong
 */
public class LazyLoadSchemaNode extends LazyLoadNode
{

    private final Schema schema;

    public LazyLoadSchemaNode(Schema schema, DefaultTreeModel treeModel, JTree treeDbs)
    {
        super(schema.getName(), treeModel, treeDbs);
        this.schema = schema;

    }

    @Override
    public void performLoadAction()
    {
        if(!schema.isLoaded())
            schema.load();
        int i = 0;
        addTables(i++, schema, this);
        addStoredProcs(i++, schema, this);
        addViews(i++, schema, this);
        addFunctions(i++, schema, this);

        treeModel.removeNodeFromParent(loadingNode);
        
    }
    
    
    private void addTables(int nodeIndex, Schema schema, DefaultMutableTreeNode schemaNode)
    {
        DefaultMutableTreeNode tables = new DefaultMutableTreeNode("Tables");
        for (Table table : schema.getTables())
            tables.add(getTableNode(table));
        treeModel.insertNodeInto(tables, schemaNode, nodeIndex);
    }

    private DefaultMutableTreeNode getTableNode(Table table)
    {
        DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(table);
        LazyLoadColumnsNode columns = new LazyLoadColumnsNode(table, treeModel, jtree );
        DefaultMutableTreeNode indexes = new LazyLoadIndexNode(table, treeModel, jtree);
        LazyLoadForeignKeyNode foreignKeys = new LazyLoadForeignKeyNode(table, treeModel, jtree);
        LazyLoadPrimaryKeyNode primaryKeys = new LazyLoadPrimaryKeyNode(table, treeModel, jtree);
        tableNode.add(columns);
        tableNode.add(indexes);
        tableNode.add(foreignKeys);
        tableNode.add(primaryKeys);
        return tableNode;
    }

    private void addStoredProcs(int nodeIndex, Schema schema, DefaultMutableTreeNode schemaNode)
    {
        DefaultMutableTreeNode procedures = new DefaultMutableTreeNode("Stored Procedures");
        for (Procedure procedure : schema.getProcedures())
        {
            DefaultMutableTreeNode procedureNode = new DefaultMutableTreeNode(procedure);
            procedureNode.add(new LazyLoadParametersNode(procedure, treeModel, jtree));
            procedures.add(procedureNode);
        }
        treeModel.insertNodeInto(procedures, schemaNode, nodeIndex);
    }

    private void addViews(int nodeIndex, Schema schema, DefaultMutableTreeNode schemaNode)
    {
        DefaultMutableTreeNode views = new DefaultMutableTreeNode("Views");
        for (Table table : schema.getViews())
            views.add(getTableNode(table));
        treeModel.insertNodeInto(views, schemaNode, nodeIndex);
    }

    private void addFunctions(int nodeIndex, Schema schema, DefaultMutableTreeNode schemaNode)
    {
        DefaultMutableTreeNode functions = new DefaultMutableTreeNode("Functions");
        for (Function function : schema.getFunctions())
        {
            DefaultMutableTreeNode functionsNode = new DefaultMutableTreeNode(function);
            for (Parameter parameter : function.getParameters())
                functionsNode.add(new DefaultMutableTreeNode(parameter));
            functions.add(functionsNode);
        }
        treeModel.insertNodeInto(functions, schemaNode, nodeIndex);
    }
}
