package com.betadb.gui.connection;

import com.betadb.gui.dbobjects.DbInfo;
import com.betadb.gui.dbobjects.Function;
import com.betadb.gui.dbobjects.Parameter;
import com.betadb.gui.dbobjects.Procedure;
import com.betadb.gui.dbobjects.Table;
import com.betadb.gui.events.Event;
import static com.betadb.gui.events.Event.DB_INFO_UPDATED;
import com.betadb.gui.events.EventListener;
import com.betadb.gui.events.EventManager;

import com.betadb.gui.exception.BetaDbException;
import java.sql.SQLException;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * @author parmstrong
 */
public class LazyDbInfoNode extends LazyLoadNode implements EventListener
{
    private final DbInfo dbInfo;
    private final JTree treeDbs;
    private final EventManager eventManager;

    public LazyDbInfoNode(DbInfo dbInfo, DefaultTreeModel treeModel, JTree treeDbs, EventManager eventManager)
    {
        super(dbInfo, treeModel);
        this.dbInfo = dbInfo;
        this.treeModel = treeModel;
        this.treeDbs = treeDbs;
        this.eventManager = eventManager;
        eventManager.addEventListener(this);

    }

    @Override
    protected void performLoadAction()
    {
        LazyDataLoader dataLoader = new LazyDataLoader(getDbInfo(), this);
        dataLoader.execute();
    }

    public DbInfo getDbInfo()
    {
        return dbInfo;
    }

    private class LazyDataLoader extends SwingWorker<DbInfo, Void>
    {
        DbInfo dbInfo;
        DefaultMutableTreeNode dbNode;

        public LazyDataLoader(DbInfo dbInfo, DefaultMutableTreeNode dbNode)
        {
            this.dbInfo = dbInfo;
            this.dbNode = dbNode;
        }

        @Override
        protected DbInfo doInBackground()
        {
            try
            {
                dbInfo.refreshToDefault();
                eventManager.fireEvent(DB_INFO_UPDATED, dbInfo);
            }
            catch (SQLException ex)
            {
                throw new BetaDbException("Error loading db info");
            }
            return dbInfo;
        }

        @Override
        public void done()
        {
        }
    }

    @Override
    public void EventOccurred(Event event, Object value)
    {
        if (event.equals(Event.DB_INFO_UPDATED))
        {
            DbInfo dbInfo = (DbInfo) value;
            if (dbInfo == this.dbInfo)
            {
                syncWithModel();
            }
        }
    }

    protected void syncWithModel()
    {

        int childCount = treeModel.getChildCount(this);
        for (int i = childCount - 1; i >= 0; i--)
            treeModel.removeNodeFromParent((DefaultMutableTreeNode) treeModel.getChild(this, i));

        int i = 0;
        addTables(i++);
        addStoredProcs(i++);
        addViews(i++);
        addFunctions(i++);
        treeDbs.expandPath(new TreePath(this.getPath()));
    }

    private void addTables(int nodeIndex)
    {
        DefaultMutableTreeNode tables = new DefaultMutableTreeNode("Tables");
        for (Table table : dbInfo.getTables())
            tables.add(getTableNode(table));
        treeModel.insertNodeInto(tables, this, nodeIndex);
    }

    private DefaultMutableTreeNode getTableNode(Table table)
    {
        DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(table);
        LazyLoadColumnsNode columns = new LazyLoadColumnsNode(dbInfo.getDbInfoDAO(), dbInfo.getName(), table, treeModel);
        DefaultMutableTreeNode indexes = new LazyLoadIndexNode(dbInfo.getDbInfoDAO(), dbInfo.getName(), table, treeModel);
        LazyLoadForeignKeyNode foreignKeys = new LazyLoadForeignKeyNode(dbInfo.getDbInfoDAO(), dbInfo.getName(), table, treeModel);
        LazyLoadPrimaryKeyNode primaryKeys = new LazyLoadPrimaryKeyNode(dbInfo.getDbInfoDAO(), dbInfo.getName(), table, treeModel);
        tableNode.add(columns);
        tableNode.add(indexes);
        tableNode.add(foreignKeys);
        tableNode.add(primaryKeys);
        return tableNode;
    }

    private void addStoredProcs(int nodeIndex)
    {
        DefaultMutableTreeNode procedures = new DefaultMutableTreeNode("Stored Procedures");
        for (Procedure procedure : dbInfo.getProcedures())
        {
            DefaultMutableTreeNode procedureNode = new DefaultMutableTreeNode(procedure);
            procedureNode.add(new LazyLoadParametersNode(dbInfo.getDbInfoDAO(), procedure, treeModel));
            procedures.add(procedureNode);
        }
        treeModel.insertNodeInto(procedures, this, nodeIndex);
    }

    private void addViews(int nodeIndex)
    {
        DefaultMutableTreeNode views = new DefaultMutableTreeNode("Views");
        for (Table table : dbInfo.getViews())
            views.add(getTableNode(table));
        treeModel.insertNodeInto(views, this, nodeIndex);
    }

    private void addFunctions(int nodeIndex)
    {
        DefaultMutableTreeNode functions = new DefaultMutableTreeNode("Functions");
        for (Function function : dbInfo.getFunctions())
        {
            DefaultMutableTreeNode functionsNode = new DefaultMutableTreeNode(function);
            for (Parameter parameter : function.getParameters())
                functionsNode.add(new DefaultMutableTreeNode(parameter));
            functions.add(functionsNode);
        }
        treeModel.insertNodeInto(functions, this, nodeIndex);
    }
}
