/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ConnectDialog.java
 *
 * Created on Jun 14, 2011, 3:57:38 PM
 */
package com.betadb.gui.connection;

import com.betadb.gui.datasource.DataSourceKey;
import com.betadb.gui.datasource.DataSourceManager;
import com.betadb.gui.datasource.DatabaseType;
import static com.betadb.gui.datasource.DatabaseType.values;
import com.betadb.gui.events.EventManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author parmstrong
 */
@Singleton
public class ConnectDialog extends javax.swing.JDialog
{	
	private final Preferences prefs;
	private final Gson gson;
	@Inject
	EventManager eventManager;
	@Inject
	DataSourceManager dataSourceManager;

	//@Inject MainWindow mainWindow;
	/**
	 * Creates new form ConnectDialog
	 */
	@Inject
	private ConnectDialog()
	{		
		initComponents();
		for (DatabaseType type : values())
			cbDatabaseType.addItem(type);
		
		this.getRootPane().setDefaultButton(btnConnect);
		prefs = Preferences.userNodeForPackage(this.getClass());
		
		gson = new Gson();
		//if someone is starting to type in a new connection disable the delete connection button.
		Component editorComponent = cbServer.getEditor().getEditorComponent();
		editorComponent.addKeyListener(new KeyAdapter()
		{
			public void keyTyped(KeyEvent e)
			{
				btnDelete.setEnabled(false);
			}
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        btnConnect = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        txtDomain = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtInstanceName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblMsg = new javax.swing.JLabel();
        cbServer = new javax.swing.JComboBox();
        btnDelete = new javax.swing.JButton();
        txtDbName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbDatabaseType = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Create Connection");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Connection Properties"));

        jLabel1.setText("Server:");

        jLabel2.setText("UserName:");

        jLabel3.setText("Password:");

        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnConnectActionPerformed(evt);
            }
        });

        jLabel5.setText("Domain:");

        jLabel4.setText("Instance Name:");

        cbServer.setEditable(true);
        cbServer.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cbServerItemStateChanged(evt);
            }
        });

        btnDelete.setText("Delete Connection");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel6.setText("Database Name:");

        jLabel7.setText("Database Type:");

        jLabel8.setText("Port:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnConnect))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtInstanceName)
                            .addComponent(txtDomain)
                            .addComponent(txtPassword)
                            .addComponent(txtUserName)
                            .addComponent(txtDbName)
                            .addComponent(cbDatabaseType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbServer, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPort)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbDatabaseType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInstanceName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete)
                    .addComponent(btnConnect, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnConnectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnConnectActionPerformed
	{//GEN-HEADEREND:event_btnConnectActionPerformed
		DataSourceManager datasourceSupplier = dataSourceManager;
		String userName = txtUserName.getText();
		String server = cbServer.getSelectedItem().toString();
		String password = new String(txtPassword.getPassword());
		String domain = txtDomain.getText();
		String instanceName = txtInstanceName.getText();
		String dbName = txtDbName.getText().isEmpty() ? null : txtDbName.getText();
		String port = txtPort.getText().isEmpty() ? null : txtPort.getText();
		DatabaseType databaseType = (DatabaseType) cbDatabaseType.getSelectedItem();
		
		try
		{
			DataSourceKey dataSourceKey = new DataSourceKey(server, instanceName, dbName);
			datasourceSupplier.getDataSource(dataSourceKey, port, databaseType, userName, password, domain);
			this.setVisible(false);
			saveConnectionToPreferences(server, userName, password, domain, instanceName, port, databaseType);
		}
		catch (Exception e)
		{
			lblMsg.setText("Error getting a connection.");
			lblMsg.setToolTipText(e.getMessage());
			lblMsg.setForeground(Color.RED);
			throw new IllegalStateException(e);
		}
	}//GEN-LAST:event_btnConnectActionPerformed
	
	private void saveConnectionToPreferences(String serverName, String userName, String password, String domain, String instanceName, String port, DatabaseType databaseType)
	{
		ConnectionInfo connectionInfo = new ConnectionInfo(serverName, userName, password, domain, instanceName, port, databaseType);
		List<ConnectionInfo> savedConnections = getSavedConnections();
		
		if (savedConnections.contains(connectionInfo))
		{			
			ConnectionInfo currentConnectionInfo = savedConnections.get(savedConnections.indexOf(connectionInfo));
			if (!EqualsBuilder.reflectionEquals(connectionInfo, currentConnectionInfo))
				savedConnections.remove(currentConnectionInfo);
			else
				return;
		}
		
		savedConnections.add(0, connectionInfo);
		String savedConnectionsJson = gson.toJson(savedConnections);
		prefs.put("savedConnections", savedConnectionsJson);
	}
	
	private void deleteConnectionFromPreferences(ConnectionInfo connectionInfo)
	{
		List<ConnectionInfo> savedConnections = getSavedConnections();
		savedConnections.remove(connectionInfo);
		String savedConnectionsJson = gson.toJson(savedConnections);
		prefs.put("savedConnections", savedConnectionsJson);
	}
	

	private void cbServerItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cbServerItemStateChanged
	{//GEN-HEADEREND:event_cbServerItemStateChanged
		if (evt.getStateChange() == ItemEvent.SELECTED)
		{
			if (cbServer.getSelectedItem() instanceof ConnectionInfo)
			{				
				ConnectionInfo connectionInfo = (ConnectionInfo) cbServer.getSelectedItem();
				txtDomain.setText(connectionInfo.getDomain());
				txtInstanceName.setText(connectionInfo.getInstanceName());
				txtUserName.setText(connectionInfo.getUserName());
				txtPort.setText(connectionInfo.getPort());
				txtPassword.setText(connectionInfo.getPassword());
				cbDatabaseType.setSelectedItem(connectionInfo.getDatabaseType());
				btnDelete.setEnabled(true);
			}
			else
			{
				btnDelete.setEnabled(false);
			}
		}
	}//GEN-LAST:event_cbServerItemStateChanged

	private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteActionPerformed
	{//GEN-HEADEREND:event_btnDeleteActionPerformed
		if (cbServer.getSelectedItem() instanceof ConnectionInfo);
		{
			ConnectionInfo connectionInfo = (ConnectionInfo) cbServer.getSelectedItem();
			deleteConnectionFromPreferences(connectionInfo);
			clear();
			cbServer.removeItem(connectionInfo);
		}
	}//GEN-LAST:event_btnDeleteActionPerformed
	
	private void clear()
	{		
		txtDomain.setText("");
		txtInstanceName.setText("");
		txtPassword.setText("");
		txtUserName.setText("");
	}
	
	public void setVisible(boolean visible)
	{
		//this.setLocationRelativeTo(mainWindow);
		lblMsg.setText("");
		if (visible)
		{
			clear();
			populateServersCombo();
		}
		
		super.setVisible(visible);
	}
	
	private void populateServersCombo()
	{
		List<ConnectionInfo> savedConnections = getSavedConnections();
		cbServer.removeAllItems();
		Collections.sort(savedConnections, (c1, c2) -> c1.getServerName().compareTo(c2.getServerName()));
		for (ConnectionInfo connectionInfo : savedConnections)
			cbServer.addItem(connectionInfo);
	}
	
	private List<ConnectionInfo> getSavedConnections()
	{
		String savedConnections = prefs.get("savedConnections", null);
		java.lang.reflect.Type collectionType = new TypeToken<List<ConnectionInfo>>()
		{
		}.getType();
		List<ConnectionInfo> connections = gson.fromJson(savedConnections, collectionType);
		return connections == null ? new ArrayList<ConnectionInfo>() : connections;
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnDelete;
    private javax.swing.JComboBox cbDatabaseType;
    private javax.swing.JComboBox cbServer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblMsg;
    private javax.swing.JTextField txtDbName;
    private javax.swing.JTextField txtDomain;
    private javax.swing.JTextField txtInstanceName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
