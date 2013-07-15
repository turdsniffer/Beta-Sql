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
import com.betadb.gui.datasource.DataSourceSupplier;
import com.betadb.gui.datasource.SQLUtils;
import com.betadb.gui.dbobjects.DbInfo;
import com.betadb.gui.jdbc.util.ResultSetUtils;
import com.betadb.gui.table.util.renderer.RendererUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author parmstrong
 */
public class ResultsPanel extends javax.swing.JPanel
{
	private DataSource ds;
	private DbInfo dbInfo;
	private PreparedStatement stmt;
	private MessagePanel messagePanel;
	private Timer timer;
	private QueryExecutor queryExecutor;

	/**
	 * Creates new form ResultsPanel
	 */
	public ResultsPanel(DbConnection connectionInfo)
	{
		initComponents();
		timer = new Timer(0, null);
		ds = DataSourceSupplier.getInstance().getDataSourceByDbId(connectionInfo.getDataSourceKey());
		dbInfo = connectionInfo.getDbInfo();
		messagePanel = new MessagePanel();
		jSplitPane1.setBottomComponent(messagePanel);
		jSplitPane1.setResizeWeight(1);
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
				if(queryExecutor != null && !queryExecutor.isDone())
					return;
				
				messagePanel.setMessage("Running query...");
				pnlResults.removeAll();
				queryExecutor = new QueryExecutor(sql);
				queryExecutor.execute();
				
			}
		});
		if(repeatInterval != null)
			timer.setDelay(repeatInterval * 1000);
		else
			timer.setRepeats(false);
		
		timer.start();
	}

	private Component getResultsTable(ResultSet rs) throws SQLException
	{
		List<String> columnNames = ResultSetUtils.getColumnNames(rs);
		List<Class> columnClasses = ResultSetUtils.getColumnClasses(rs);

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		Object[] row;


		while (rs.next())
		{
			row = new Object[columnNames.size()];
			for (int i = 1; i <= row.length; i++)
				row[i - 1] = rs.getObject(i);
			
			data.add(row);
		}

		ResultsTableModel resultsTableModel = new ResultsTableModel(columnNames, columnClasses, data);


		final JTable table = new JTable(resultsTableModel);
		table.setTransferHandler(new ResultsTableTransferHandler());
		table.setAutoCreateRowSorter(true);
		table.setCellSelectionEnabled(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		RowNumberColumnMouseListener rowNumberColumnMouseListener = new RowNumberColumnMouseListener();
		table.addMouseListener(rowNumberColumnMouseListener);
		table.addMouseMotionListener(rowNumberColumnMouseListener);

		table.setComponentPopupMenu(new ResultTablePopup());	
		RendererUtils.formatColumns(table, new ResultsPanelTableCellRenderer());
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		final ListSelectionModel selectionModel = table.getColumnModel().getSelectionModel();
		table.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent event)
			{
				if(selectionModel.isSelectedIndex(table.convertColumnIndexToView(0)))
					selectionModel.removeSelectionInterval(table.convertColumnIndexToView(0), table.convertColumnIndexToView(0));
			}
		});


		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel("Rows: " + data.size());
		JScrollPane jScrollPane = new JScrollPane(table);
		resultsPanel.add(label);
		resultsPanel.add(jScrollPane);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		jScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		jScrollPane.setPreferredSize(new Dimension(100, 125));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		return resultsPanel;
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
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        scrlResults = new javax.swing.JScrollPane();
        pnlResults = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        jSplitPane1.setDividerLocation(475);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        scrlResults.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        pnlResults.setAlignmentX(0.0F);
        pnlResults.setLayout(new javax.swing.BoxLayout(pnlResults, javax.swing.BoxLayout.PAGE_AXIS));
        scrlResults.setViewportView(pnlResults);

        jSplitPane1.setTopComponent(scrlResults);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pnlResults;
    private javax.swing.JScrollPane scrlResults;
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

			List<Component> results = new ArrayList<Component>();

			Connection conn = null;
			stmt = null;
			ResultSet rs = null;
			String message = "Query Finished Successfully";

			try
			{
				conn = ds.getConnection();
				conn.setCatalog(dbInfo.getDbName());				

				String[] statements = sql.split("(?im)^\\s*GO\\s*$");
				for (String sql : statements)
				{
					stmt = conn.prepareStatement(sql);
					stmt.setFetchSize(500);
					stmt.execute();

					do
					{
						rs = stmt.getResultSet();
						if (rs == null)
							continue;

						results.add(getResultsTable(rs));

					}
					while (!((stmt.getMoreResults() == false) && (stmt.getUpdateCount() == -1)));

					SQLUtils.close(stmt);
					SQLUtils.close(rs);
				}
			}
			catch (Exception e)
			{
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				sw.toString();
				message = e.getMessage() + "\n" + sw.toString();
			}
			finally
			{
				SQLUtils.close(conn, stmt, rs);
			}

			messagePanel.addMessage(message);

			return results;

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
			catch (Exception ex)
			{
				Logger.getLogger(ResultsPanel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
