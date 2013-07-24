package com.betadb.gui.connection;

import com.betadb.gui.dao.DbInfoDAO;
import com.betadb.gui.datasource.DataSourceSupplier;
import com.betadb.gui.dbobjects.DbInfo;
import com.betadb.gui.dbobjects.DbObject;
import com.betadb.gui.dbobjects.Table;
import com.betadb.gui.events.Event;
import static com.betadb.gui.events.Event.*;
import com.betadb.gui.events.EventListener;
import com.betadb.gui.events.EventManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
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
public class ConnectionsPanel extends javax.swing.JPanel implements EventListener
{
	private static ConnectionsPanel connectionsPanel;
	private static DataSourceSupplier dataSourceSupplier = DataSourceSupplier.getInstance();
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;

	public static ConnectionsPanel getInstance()
	{
		if (connectionsPanel == null)
			connectionsPanel = new ConnectionsPanel();
		return connectionsPanel;
	}

	/**
	 * Creates new form ConnectionsPanel
	 */
	private ConnectionsPanel()
	{

		EventManager.getInstance().addEventListener(this);
		initComponents();
		treeDbs.setEditable(false);
		treeDbs.addMouseListener(new NodeMouseListener());
		treeDbs.addTreeExpansionListener(new NodeExpansionListener());
		treeDbs.setCellRenderer(new ConnectionsTreeCellRenderer());
	}

	private void addDataSource(String dataSourceKey)
	{
		try
		{			
			DataSource dataSource = dataSourceSupplier.getDataSourceByDbId(dataSourceKey);

			DefaultMutableTreeNode server = new DefaultMutableTreeNode(dataSourceKey);
			addDataBases(dataSource, server);
			DefaultTreeModel model = (DefaultTreeModel) treeDbs.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			model.insertNodeInto(server, root, root.getChildCount());
			treeDbs.scrollPathToVisible(new TreePath(server.getPath()));
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(this, "Error getting databases." + ex.getMessage(), "Connect Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void addDataBases(DataSource dataSource, DefaultMutableTreeNode top) throws SQLException
	{
		List<DbInfo> infos = new ArrayList<DbInfo>();

		DbInfoDAO dao = new DbInfoDAO(dataSource);
		infos = dao.getDatabases();
		for (DbInfo dbInfo : infos)
			top.add(new LazyDbInfoNode(dbInfo, dataSource, treeModel, treeDbs));
	}

	@Override
	public void EventOccurred(Event event, Object value)
	{
		if (event == DATA_SOURCE_ADDED)
		{
			String dataSourceKey = (String) value;
			addDataSource(dataSourceKey);
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
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        dbPopupMenu = new javax.swing.JPopupMenu();
        btnConnect = new javax.swing.JMenuItem();
        refresh = new javax.swing.JMenuItem();
        btnFind = new javax.swing.JMenuItem();
        dbObjectPopupMenu = new javax.swing.JPopupMenu();
        btnScript = new javax.swing.JMenuItem();
        jToolBar1 = new javax.swing.JToolBar();
        btnAddDataSource = new javax.swing.JButton();
        pnlConnections = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        root = new DefaultMutableTreeNode("Root Node");
        treeModel = new DefaultTreeModel(root);
        treeDbs = new javax.swing.JTree(treeModel);
        treeDbs.setRootVisible(false);
        treeDbs.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        btnConnect.setText("New Connection");
        btnConnect.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnConnectActionPerformed(evt);
            }
        });
        dbPopupMenu.add(btnConnect);

        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                refreshActionPerformed(evt);
            }
        });
        dbPopupMenu.add(refresh);

        btnFind.setText("Find...");
        btnFind.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFindActionPerformed(evt);
            }
        });
        dbPopupMenu.add(btnFind);

        btnScript.setText("Script...");
        btnScript.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnScriptActionPerformed(evt);
            }
        });
        dbObjectPopupMenu.add(btnScript);

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
		ConnectDialog.getInstance().setVisible(true);
	}//GEN-LAST:event_btnAddDataSourceActionPerformed

	private void btnConnectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnConnectActionPerformed
	{//GEN-HEADEREND:event_btnConnectActionPerformed

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeDbs.getLastSelectedPathComponent();

		if (node == null)
			return;
		DbInfo dbInfo = ((LazyDbInfoNode) node).getDbInfo();
		treeDbs.expandPath(new TreePath(node.getPath()));
		//loadLazyData(node, dbInfo, false);
		String dataSourceKey = ((DefaultMutableTreeNode) node.getParent()).getUserObject().toString();
		EventManager.getInstance().fireEvent(SQL_CONNECTION_REQUESTED, new DbConnection(dbInfo, dataSourceKey));
	}//GEN-LAST:event_btnConnectActionPerformed

	private void treeDbsValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_treeDbsValueChanged
	{//GEN-HEADEREND:event_treeDbsValueChanged
		DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) evt.getPath().getLastPathComponent();
		Object userObject = lastPathComponent.getUserObject();

		if (userObject instanceof DbObject)
			EventManager.getInstance().fireEvent(DB_OBJECT_SELECTED, userObject);
	}//GEN-LAST:event_treeDbsValueChanged

	private void refreshActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_refreshActionPerformed
	{//GEN-HEADEREND:event_refreshActionPerformed
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeDbs.getLastSelectedPathComponent();

		if (node == null)
			return;
		LazyLoadNode dbNode = (LazyLoadNode)node;
		dbNode.load(true);
	}//GEN-LAST:event_refreshActionPerformed

	private void btnScriptActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnScriptActionPerformed
	{//GEN-HEADEREND:event_btnScriptActionPerformed
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeDbs.getLastSelectedPathComponent();
		DbObject userObject = (DbObject) node.getUserObject();
		while (!(node.getUserObject() instanceof DbInfo))
			node = (DefaultMutableTreeNode) node.getParent();
		DbInfo dbInfo = (DbInfo) node.getUserObject();
		String server = ((DefaultMutableTreeNode) node.getParent()).getUserObject().toString();
		DataSource dataSource = dataSourceSupplier.getDataSourceByDbId(server);

		DbInfoDAO dbInfoDAO = new DbInfoDAO(dataSource);
		try
		{
			String script = dbInfoDAO.getScript(userObject, dbInfo.getDbName());
			EventManager.getInstance().fireEvent(SQL_CONNECTION_REQUESTED, new DbConnection(dbInfo, server, script));
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}//GEN-LAST:event_btnScriptActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFindActionPerformed
    {//GEN-HEADEREND:event_btnFindActionPerformed
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeDbs.getLastSelectedPathComponent();

		if (node == null)
			return;
		
		LazyDbInfoNode dbNode = (LazyDbInfoNode)node;
		dbNode.load();
		FindObjectDialog findObjectDialog = new FindObjectDialog(dbNode.getDbInfo());
		findObjectDialog.setLocationRelativeTo(this);
		findObjectDialog.setVisible(true);
    }//GEN-LAST:event_btnFindActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddDataSource;
    private javax.swing.JMenuItem btnConnect;
    private javax.swing.JMenuItem btnFind;
    private javax.swing.JMenuItem btnScript;
    private javax.swing.JPopupMenu dbObjectPopupMenu;
    private javax.swing.JPopupMenu dbPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
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
		public void treeCollapsed(TreeExpansionEvent tee){}
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
					if (lastPathComponent instanceof LazyDbInfoNode)
						dbPopupMenu.show(e.getComponent(), e.getX(), e.getY());
					if (userObject instanceof DbObject)
					{
						if (userObject instanceof Table)
							btnScript.setEnabled(false);
						else
							btnScript.setEnabled(true);

						dbObjectPopupMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		}
	}	
}
