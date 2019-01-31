package com.betadb.gui.connection;

import com.betadb.gui.dbobjects.DbInfo;
import com.betadb.gui.dbobjects.Schema;
import com.betadb.gui.events.Event;
import static com.betadb.gui.events.Event.DB_INFO_UPDATED;
import com.betadb.gui.events.EventListener;
import com.betadb.gui.events.EventManager;

import com.betadb.gui.exception.BetaDbException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final EventManager eventManager;

    public LazyDbInfoNode(DbInfo dbInfo, DefaultTreeModel treeModel, JTree treeDbs, EventManager eventManager)
    {
        super(dbInfo, treeModel, treeDbs);
        this.dbInfo = dbInfo;
        this.treeModel = treeModel;
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
                syncWithModel();
        }
    }

    protected void syncWithModel()
    {
        int childCount = treeModel.getChildCount(this);
        List<DefaultMutableTreeNode> curSchemaNodes = new ArrayList();
        for (int i = childCount - 1; i >= 0; i--)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeModel.getChild(this, i);
            curSchemaNodes.add(node);
            treeModel.removeNodeFromParent((DefaultMutableTreeNode) treeModel.getChild(this, i));
        }
        
        
        List<Schema> schemas = dbInfo.getSchemas();
        int i = 0;
        for (Schema schema : schemas)
        {
            Optional<DefaultMutableTreeNode> matchingSchemaNode = curSchemaNodes.stream().filter(node -> 
                node.getUserObject() == schema.getName()
            ).findFirst();
            if(matchingSchemaNode.isPresent())
                treeModel.insertNodeInto(matchingSchemaNode.get(), this, i);  
            else
            {
                DefaultMutableTreeNode schemaNode = new LazyLoadSchemaNode(schema, this.treeModel, jtree);
                treeModel.insertNodeInto(schemaNode, this, i);
            }
            i++;            
        }
        this.jtree.expandPath(new TreePath(this.getPath()));
    }
}
