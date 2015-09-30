/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ResultsPanel.java
 *
 * Created on Jun 21, 2011, 3:02:47 PM
 */
package com.betadb.gui.sql;

import com.betadb.gui.connection.DbConnection;
import com.betadb.gui.datasource.DataSourceManager;
import static com.betadb.gui.datasource.SQLUtils.close;
import com.betadb.gui.dbobjects.DbInfo;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.sql.DataSource;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author parmstrong
 */
public class ResultsPanel extends javax.swing.JPanel
{
	private DataSource ds;
	private DbInfo dbInfo;
	private Connection conn;
	private Statement stmt;

	private MessagePanel messagePanel;
	private Timer timer;
	private QueryExecutor queryExecutor;
	@Inject
	ResultTablePopup resultTablePopup;
	@Inject
	DataSourceManager dataSourceManager;
	@Inject
	Provider<ResultSetPanel> resultSetPanelProvider;

	/**
	 * Creates new form ResultsPanel
	 */
	@Inject
	public ResultsPanel(MessagePanel messagePanel)
	{
		initComponents();
		timer = new Timer(0, null);
		this.messagePanel = messagePanel;
		jSplitPane1.setBottomComponent(messagePanel);
		jSplitPane1.setResizeWeight(1);
	}

	public void setDbConnectInfo(DbConnection connectionInfo)
	{
		try
		{
			ds = dataSourceManager.getDataSourceByDbId(connectionInfo.getDataSourceKey());
			dbInfo = connectionInfo.getDbInfo();
			resetConnection();

		}
		catch (SQLException ex)
		{
			Logger.getLogger(ResultsPanel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void resetConnection() throws SQLException
	{
		conn = ds.getConnection();
		conn.setCatalog(dbInfo.getDbName());
	}

	public void executeSql(String sql)
	{
		executeSql(sql, null);
	}

	public void executeSql(final String sql, Integer repeatInterval)
	{
		timer.stop();
		timer = new Timer(0, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				if (queryExecutor != null && !queryExecutor.isDone())
					return;

				messagePanel.setMessage("Running query...");
				pnlResults.removeAll();
				queryExecutor = new QueryExecutor(sql);
				queryExecutor.execute();

			}
		});
		if (repeatInterval != null)
			timer.setDelay(repeatInterval * 1000);
		else
			timer.setRepeats(false);

		timer.start();
	}

	public void cancelQuery()
	{
		messagePanel.addMessage("Attempting to cancel query");
		try
		{
			if (stmt != null && !stmt.isClosed())
				stmt.cancel();
		}
		catch (SQLException ex)
		{
			messagePanel.addMessage(ex.getMessage());
		}
		timer.stop();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jSplitPane1 = new javax.swing.JSplitPane();
        pnlResults = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        jSplitPane1.setDividerLocation(475);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        pnlResults.setAlignmentX(0.0F);
        pnlResults.setLayout(new javax.swing.BoxLayout(pnlResults, javax.swing.BoxLayout.PAGE_AXIS));
        jSplitPane1.setLeftComponent(pnlResults);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pnlResults;
    // End of variables declaration//GEN-END:variables

	private class QueryExecutor extends SwingWorker<List<Component>, List<Component>>
	{
		String sql;

		public QueryExecutor(String sql)
		{
			this.sql = sql;
		}

		@Override
		protected List<Component> doInBackground() throws Exception
		{
			List<Component> resultTables = new ArrayList<>();

			ResultSet rs = null;
			String message = "Query Finished Successfully";

			try
			{
				stmt = conn.createStatement();
				stmt.setFetchSize(500);
				String[] statements = sql.split("(?im)^\\s*GO\\s*$");
				for (String sql : statements)
				{
					stmt.execute(sql);
					do
					{
						rs = stmt.getResultSet();

						if (stmt.getWarnings() != null)
							for (Throwable warning : stmt.getWarnings())
								message += "\n" + warning.getMessage();

						if (rs == null)
							continue;
						ResultSetPanel resultSetPanel = resultSetPanelProvider.get();
						resultSetPanel.setData(rs);
						resultTables.add(resultSetPanel);

					}
					while (!((stmt.getMoreResults() == false) && (stmt.getUpdateCount() == -1)));
					close(rs);
				}
			}
			catch (SQLException e)
			{
				if (e.getMessage().contains("Connection reset"))
				{
					message = "Connection reset, try rerunning query";
					resetConnection();
				}
				else
				{
					message = e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e);
				}
			}
			catch (Exception e)
			{
				message = e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e);
			}
			finally
			{
				close(rs);
				close(stmt);
			}

			messagePanel.addMessage(message);

			JTabbedPane jTabbedPane = new JTabbedPane();
			for (int i = 0; i < resultTables.size(); i++)
				jTabbedPane.addTab("rs" + i, resultTables.get(i));

			return Collections.singletonList(jTabbedPane);

		}

		@Override
		protected void done()
		{
			try
			{
				List<Component> results = get();
				for (Component component : results)
					pnlResults.add(component);

				pnlResults.revalidate();

			}
			catch (InterruptedException | ExecutionException ex)
			{
				getLogger(ResultsPanel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
