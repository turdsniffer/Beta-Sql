package com.betadb.gui;

import com.betadb.gui.connection.DbConnection;
import com.betadb.gui.connection.ConnectionsPanel;
import com.betadb.gui.events.Event;
import com.betadb.gui.events.EventListener;
import com.betadb.gui.events.EventManager;
import com.betadb.gui.objectdetail.ObjectDetailsPanel;
import com.betadb.gui.sql.SqlPanel;
import java.awt.BorderLayout;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.util.ViewMap;
import static com.betadb.gui.events.Event.*;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import java.awt.Dimension;
import static java.awt.EventQueue.invokeLater;
import static java.awt.Toolkit.getDefaultToolkit;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static net.infonode.docking.util.DockingUtil.createRootWindow;

/**
 *
 * @author parmstrong
 */
public class MainWindow extends javax.swing.JFrame implements EventListener
{
	private final RootWindow rootWindow;
	private TabWindow tabWindow;
	@Inject
	private Provider<SqlPanel> sqlPanelProvider;

	/**
	 * Creates new form Main
	 */
	@Inject
	public MainWindow(EventManager eventManager, ConnectionsPanel connectionsPanel, ObjectDetailsPanel objectDetailsPanel)
	{


		this.setTitle("Beta-DB SQL Editor");
		initComponents();
		URL resource = getClass().getResource("/com/betadb/gui/icons/betadb.png");
		this.setIconImage(getDefaultToolkit().getImage(resource));
		this.setSize(new Dimension(1900, 1000));
		eventManager.addEventListener(this);
		this.tabWindow = new TabWindow();

		ViewMap viewMap = new ViewMap();
		viewMap.addView(0, new View("Connections", null, connectionsPanel));
		viewMap.addView(1, new View("ObjectDetails", null, objectDetailsPanel));

		rootWindow = createRootWindow(viewMap, true);
		rootWindow.setWindow(new SplitWindow(true, 0.15f, new SplitWindow(false, .80f, viewMap.getView(0), viewMap.getView(1)), tabWindow));

		rootWindow.setVisible(true);
		this.setLayout(new BorderLayout());
		this.add(rootWindow, BorderLayout.CENTER);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 915, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[])
	{
		invokeLater(new Runnable()
		{
			public void run()
			{
				Injector injector = Guice.createInjector(new GuiceModule());
				MainWindow instance = injector.getInstance(MainWindow.class);
				instance.setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

	@Override
	public void EventOccurred(Event event, Object value)
	{
		if (event == SQL_CONNECTION_REQUESTED)
		{
			DbConnection connectionInfo = (DbConnection) value;
			SqlPanel sqlPanel = sqlPanelProvider.get();
			sqlPanel.setDbConnectInfo(connectionInfo);
			View view = new View(connectionInfo.getDataSourceKey() + " (" + connectionInfo.getDbInfo().getDbName() + ")", null, sqlPanel);
			

			tabWindow.addTab(view);
		}
	}
}
