package com.betadb.gui.connection;

import com.betadb.gui.dao.DbInfoDAO;
import com.betadb.gui.datasource.DataSourceKey;
import com.betadb.gui.datasource.DataSourceManager;
import com.betadb.gui.dbobjects.DbInfo;
import com.betadb.gui.dbobjects.DbObject;
import com.betadb.gui.dbobjects.DbObjectType;
import com.betadb.gui.dbobjects.Server;
import com.betadb.gui.dbobjects.Table;
import com.betadb.gui.events.Event;
import static com.betadb.gui.events.Event.*;
import com.betadb.gui.events.EventListener;
import com.betadb.gui.events.EventManager;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import javax.swing.JMenuItem;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JSeparator;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author parmstrong
 */
@Singleton
public class ConnectionsPanel extends javax.swing.JPanel implements EventListener
{
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private final EventManager eventManager;
    private final Multimap<DbObjectType, JMenuItem> contextMenuButtonMap = HashMultimap.create();
    private final List<JMenuItem> defaultContextButtons;

    @Inject
    FindObjectDialog findObjectDialog;
    @Inject
    ConnectDialog connectDialog;
    @Inject
    TablePrivileges tablePrivileges;
    @Inject
    private DataSourceManager dataSourceManager;

    /**
     * Creates new form ConnectionsPanel
     */
    @Inject
    private ConnectionsPanel(EventManager eventManager)
    {
        eventManager.addEventListener(this);
        this.eventManager = eventManager;
        initComponents();
        treeDbs.setEditable(false);
        treeDbs.addMouseListener(new NodeMouseListener());
        treeDbs.addTreeExpansionListener(new NodeExpansionListener());
        treeDbs.setCellRenderer(new ConnectionsTreeCellRenderer());
        defaultContextButtons = Lists.newArrayList(btnConnect, btnFind, refresh);
        contextMenuButtonMap.putAll(DbObjectType.FUNCTION, Lists.newArrayList(btnScript));
        contextMenuButtonMap.putAll(DbObjectType.PROCEDURE, Lists.newArrayList(btnScript));
        contextMenuButtonMap.putAll(DbObjectType.VIEW, Lists.newArrayList(btnScript, btnPrivileges));
        contextMenuButtonMap.putAll(DbObjectType.TABLE, Lists.newArrayList(btnPrivileges));

    }

    private void addServer(DataSourceKey dataSourceKey)
    {
        DataSource ds = dataSourceManager.getDataSourceByDbId(dataSourceKey);
        DbInfoDAO dbInfoDAO = new DbInfoDAO(ds, eventManager);
        Server server = new Server(dataSourceKey.getDbServerName(), dataSourceKey.getInstanceName(), dataSourceKey.getDbName(), dbInfoDAO);
        
        DefaultMutableTreeNode serverNode = new DefaultMutableTreeNode(server);

        for (DbInfo dbInfo : server.getDbs())
            serverNode.add(new LazyDbInfoNode(dbInfo, treeModel, treeDbs, eventManager));

        DefaultTreeModel model = (DefaultTreeModel) treeDbs.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        model.insertNodeInto(serverNode, root, root.getChildCount());
        treeDbs.scrollPathToVisible(new TreePath(serverNode.getPath()));
    }

    @Override
    public void EventOccurred(Event event, Object value)
    {
        if (event == DATA_SOURCE_ADDED)
        {
            DataSourceKey dataSourceKey = (DataSourceKey) value;

            addServer(dataSourceKey);
        }
        if (event == DB_OBJECT_SELECTED)
        {
            DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) treeDbs.getLastSelectedPathComponent();
            if (lastPathComponent == null)
                return;
            Object currentlySelectedNode = lastPathComponent.getUserObject();
            if (currentlySelectedNode != value)
            {
                for (Enumeration e = root.depthFirstEnumeration(); e.hasMoreElements();)
                {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                    if (node.getUserObject() == value)
                    {
                        TreePath treePath = new TreePath(node.getPath());
                        treeDbs.setSelectionPath(treePath);
                        treeDbs.scrollPathToVisible(treePath);
                    }
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        dbObjectPopupMenu = new javax.swing.JPopupMenu();
        btnScript = new javax.swing.JMenuItem();
        btnPrivileges = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        btnConnect = new javax.swing.JMenuItem();
        refresh = new javax.swing.JMenuItem();
        btnFind = new javax.swing.JMenuItem();
        jToolBar1 = new javax.swing.JToolBar();
        btnAddDataSource = new javax.swing.JButton();
        pnlConnections = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        root = new DefaultMutableTreeNode("Root Node");
        treeModel = new DefaultTreeModel(root);
        treeDbs = new javax.swing.JTree(treeModel);
        treeDbs.setRootVisible(false);
        treeDbs.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        btnScript.setText("Script...");
        btnScript.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnScriptActionPerformed(evt);
            }
        });
        dbObjectPopupMenu.add(btnScript);

        btnPrivileges.setText("Privileges");
        btnPrivileges.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrivilegesActionPerformed(evt);
            }
        });
        dbObjectPopupMenu.add(btnPrivileges);
        dbObjectPopupMenu.add(jSeparator1);

        btnConnect.setText("New Connection");
        btnConnect.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnConnectActionPerformed(evt);
            }
        });
        dbObjectPopupMenu.add(btnConnect);

        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                refreshActionPerformed(evt);
            }
        });
        dbObjectPopupMenu.add(refresh);

        btnFind.setText("Find...");
        btnFind.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFindActionPerformed(evt);
            }
        });
        dbObjectPopupMenu.add(btnFind);

        jToolBar1.setRollover(true);

        btnAddDataSource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/betadb/gui/icons/connect.png"))); // NOI18N
        btnAddDataSource.setFocusable(false);
        btnAddDataSource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddDataSource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddDataSource.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddDataSourceActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAddDataSource);

        pnlConnections.setLayout(new javax.swing.BoxLayout(pnlConnections, javax.swing.BoxLayout.LINE_AXIS));

        treeDbs.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                treeDbsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treeDbs);

        pnlConnections.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
            .addComponent(pnlConnections, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlConnections, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void btnAddDataSourceActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddDataSourceActionPerformed
	{//GEN-HEADEREND:event_btnAddDataSourceActionPerformed
        connectDialog.setLocationRelativeTo(btnConnect);
        connectDialog.setVisible(true);

	}//GEN-LAST:event_btnAddDataSourceActionPerformed

	private void btnConnectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnConnectActionPerformed
	{//GEN-HEADEREND:event_btnConnectActionPerformed

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeDbs.getLastSelectedPathComponent();

        if (node == null)
            return;
        if (node instanceof LazyDbInfoNode)
            treeDbs.expandPath(new TreePath(node.getPath()));

        Server server = getServerForNode(node);
        LazyDbInfoNode dbInfoNode = getDbInfoNode(node);

        eventManager.fireEvent(SQL_CONNECTION_REQUESTED, new DbConnection(server, dbInfoNode.getDbInfo().getName()));
	}//GEN-LAST:event_btnConnectActionPerformed

	private void treeDbsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeDbsValueChanged
	{//GEN-HEADEREND:event_treeDbsValueChanged
        DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) evt.getPath().getLastPathComponent();
        Object userObject = lastPathComponent.getUserObject();

        if (userObject instanceof DbObject)
            eventManager.fireEvent(DB_OBJECT_SELECTED, userObject);
	}//GEN-LAST:event_treeDbsValueChanged

	private void refreshActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_refreshActionPerformed
	{//GEN-HEADEREND:event_refreshActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeDbs.getLastSelectedPathComponent();

        if (node == null)
            return;
        LazyDbInfoNode dbInfoNode = getDbInfoNode(node);
        dbInfoNode.load(true);
	}//GEN-LAST:event_refreshActionPerformed

	private void btnScriptActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnScriptActionPerformed
	{//GEN-HEADEREND:event_btnScriptActionPerformed
        try
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeDbs.getLastSelectedPathComponent();
            DataSource dataSource = getDataSourceForNode(node);
            DbInfo dbInfo = getDbInfoForNode(node);
            Server server = getServerForNode(node);
            DbInfoDAO dbInfoDAO = new DbInfoDAO(dataSource, eventManager);
            DbObject userObject = (DbObject) node.getUserObject();

            String script = dbInfoDAO.getScript(userObject, dbInfo.getName());
            eventManager.fireEvent(SQL_CONNECTION_REQUESTED, new DbConnection(server, script));
        }
        catch (SQLException ex)
        {
            showMessageDialog(this, ex.getMessage());
        }
	}//GEN-LAST:event_btnScriptActionPerformed

    private Server getServerForNode(DefaultMutableTreeNode node)
    {
        LazyDbInfoNode dbInfoNode = getDbInfoNode(node);
        return (Server) ((DefaultMutableTreeNode) dbInfoNode.getParent()).getUserObject();
    }

    private DataSource getDataSourceForNode(DefaultMutableTreeNode node)
    {
        LazyDbInfoNode dbInfoNode = getDbInfoNode(node);
        Server server = (Server) ((DefaultMutableTreeNode) dbInfoNode.getParent()).getUserObject();
        return dataSourceManager.getDataSourceByDbId(new DataSourceKey(server.getServerName(), server.getInstanceName(), dbInfoNode.getDbInfo().getName()));
    }

    private DbInfo getDbInfoForNode(DefaultMutableTreeNode node)
    {
        return getDbInfoNode(node).getDbInfo();
    }

    private LazyDbInfoNode getDbInfoNode(DefaultMutableTreeNode node)
    {
        while (!(node instanceof LazyDbInfoNode))
            node = (DefaultMutableTreeNode) node.getParent();
        return (LazyDbInfoNode) node;
    }


    private void btnFindActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFindActionPerformed
    {//GEN-HEADEREND:event_btnFindActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeDbs.getLastSelectedPathComponent();

        if (node == null)
            return;
        LazyDbInfoNode dbNode = getDbInfoNode(node);
        dbNode.load();
        findObjectDialog.show(dbNode.getDbInfo());
        findObjectDialog.setLocationRelativeTo(this);
        findObjectDialog.setVisible(true);
    }//GEN-LAST:event_btnFindActionPerformed

    private void btnPrivilegesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrivilegesActionPerformed
    {//GEN-HEADEREND:event_btnPrivilegesActionPerformed

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeDbs.getLastSelectedPathComponent();

        if (!(node.getUserObject() instanceof Table))
            return;
        Table table = (Table) node.getUserObject();
        DataSource dataSource = getDataSourceForNode(node);
        DbInfoDAO dbInfoDAO = new DbInfoDAO(dataSource, eventManager);
        DbInfo dbInfo = getDbInfoForNode(node);
        List<Map<String, String>> tablePrivileges = dbInfoDAO.getTablePrivileges(table);
        this.tablePrivileges.show(tablePrivileges);


    }//GEN-LAST:event_btnPrivilegesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddDataSource;
    private javax.swing.JMenuItem btnConnect;
    private javax.swing.JMenuItem btnFind;
    private javax.swing.JMenuItem btnPrivileges;
    private javax.swing.JMenuItem btnScript;
    private javax.swing.JPopupMenu dbObjectPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel pnlConnections;
    private javax.swing.JMenuItem refresh;
    private javax.swing.JTree treeDbs;
    // End of variables declaration//GEN-END:variables

    private class NodeExpansionListener implements TreeExpansionListener
    {
        @Override
        public void treeExpanded(TreeExpansionEvent tee)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tee.getPath().getLastPathComponent();
            if (node instanceof LazyLoadNode)
                ((LazyLoadNode) node).load();
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent tee)
        {
        }
    }

    private class NodeMouseListener extends MouseAdapter
    {
        public void mouseReleased(MouseEvent e)
        {
            mousePressed(e);
        }

        public void mousePressed(MouseEvent e)
        {
            if (e.isPopupTrigger())
            {
                int selRow = treeDbs.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = treeDbs.getPathForLocation(e.getX(), e.getY());
                treeDbs.setSelectionPath(selPath);
                if (selRow != -1)
                {
                    DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                    Object userObject = lastPathComponent.getUserObject();
                    dbObjectPopupMenu.removeAll();

                    if (userObject instanceof DbObject)
                    {
                        for (JMenuItem menuItem : contextMenuButtonMap.get(((DbObject) userObject).getObjectType()))
                            dbObjectPopupMenu.add(menuItem);
                        dbObjectPopupMenu.add(new JSeparator());
                    }

                    if (userObject instanceof DbObject || userObject instanceof DbInfo)
                        for (JMenuItem defaultContextButton : defaultContextButtons)
                            dbObjectPopupMenu.add(defaultContextButton);
                    dbObjectPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }
}
