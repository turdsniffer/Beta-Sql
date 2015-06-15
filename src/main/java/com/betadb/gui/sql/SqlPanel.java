/*
 * SqlPanel.java
 *
 * Created on Jun 21, 2011, 3:04:57 PM
 */

package com.betadb.gui.sql;

import com.betadb.gui.connection.DbConnection;
import com.betadb.gui.queryanalyzer.SqlServerQueryAnalyzer;
import com.google.inject.Inject;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import static java.lang.Integer.parseInt;
import static java.util.logging.Logger.getLogger;
import static javax.swing.JOptionPane.showInputDialog;
import javax.swing.JScrollPane;
import static javax.swing.KeyStroke.getKeyStroke;

/**
 *
 * @author parmstrong
 */
public class SqlPanel extends javax.swing.JPanel 
{
	private final EditorPanel editorPanel;
	private final ResultsPanel resultsPanel;
	private final JFileChooser fileChooser;
	private final JSplitPane splitPane;
	private final SqlServerQueryAnalyzer queryAnalyzer;
	private String filePath;

	@Inject
    public SqlPanel(ResultsPanel resultsPanel, EditorPanel editorPanel, SqlServerQueryAnalyzer queryAnalyzer)
	{			
        initComponents();	
		fileChooser = new JFileChooser();
		this.resultsPanel = resultsPanel;
		this.editorPanel = editorPanel;
		this.queryAnalyzer = queryAnalyzer;
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, resultsPanel);
		splitPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlMain.add(splitPane);	
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(getKeyStroke("F5"),"execute");
		this.getActionMap().put("execute", new AbstractAction() 
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				executeSql(false);
			}
		});
		
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(getKeyStroke("control S"), "PRESS");
        this.getActionMap().put("PRESS", new AbstractAction() 
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				save(false);
			}
		});
    }

	public void setDbConnectInfo(DbConnection connectionInfo)
	{
		resultsPanel.setDbConnectInfo(connectionInfo);
		editorPanel.setDbConnectInfo(connectionInfo);
		queryAnalyzer.setDbInfo(connectionInfo);
	}

	private void executeSql(boolean executeAll)
	{
		executeSql(executeAll, null);
	}
	
	private void executeSql(boolean executeAll, Integer repeatInterval)
	{		
		String sql = editorPanel.getSql(executeAll);
		if(btnAnalyzeQuery.isSelected())
		{			
			JScrollPane jScrollPane = new JScrollPane(queryAnalyzer);
			splitPane.setBottomComponent(jScrollPane);
			queryAnalyzer.analyze(sql);
		}
		else
		{
			resultsPanel.executeSql(sql, repeatInterval);			
			splitPane.setBottomComponent(resultsPanel);
		}
	}
	
	private void save(boolean saveAs)
	{
		String sql = editorPanel.getSql(true);
		File file = null;
		if (filePath == null || saveAs)
		{
			int response = fileChooser.showSaveDialog(this);
			if (response == JFileChooser.APPROVE_OPTION)
			{
				file = fileChooser.getSelectedFile();
				filePath = file.getPath();
			}
			else if(response == JFileChooser.CANCEL_OPTION)
				return;
		}
		else
			file = new File(filePath);
		
		
		FileWriter fw = null;
		try
		{
			
			fw = new FileWriter(file);
			fw.write(sql);
		}
		catch (IOException ex)
		{
			getLogger(SqlPanel.class.getName()).log(Level.SEVERE, null, ex);
		}
		finally
		{
			try
			{
				fw.close();
			}
			catch (IOException ex)
			{
				getLogger(SqlPanel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}	
	}
	
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jToolBar1 = new javax.swing.JToolBar();
        btnExecute = new javax.swing.JButton();
        btnExecuteAll = new javax.swing.JButton();
        btnRepeatExecution = new javax.swing.JButton();
        btnCancelQuery = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnSaveAs = new javax.swing.JButton();
        btnOpenFile = new javax.swing.JButton();
        synchObjectsPane = new javax.swing.JButton();
        btnAnalyzeQuery = new javax.swing.JToggleButton();
        pnlMain = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        jToolBar1.setRollover(true);
        jToolBar1.setAlignmentX(0.0F);

        btnExecute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/betadb/gui/icons/execute.png"))); // NOI18N
        btnExecute.setToolTipText("Run");
        btnExecute.setFocusable(false);
        btnExecute.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExecute.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExecute.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExecuteActionPerformed(evt);
            }
        });
        jToolBar1.add(btnExecute);

        btnExecuteAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/betadb/gui/icons/executeAll.png"))); // NOI18N
        btnExecuteAll.setToolTipText("Run All");
        btnExecuteAll.setFocusable(false);
        btnExecuteAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExecuteAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExecuteAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExecuteAllActionPerformed(evt);
            }
        });
        jToolBar1.add(btnExecuteAll);

        btnRepeatExecution.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/betadb/gui/icons/executeRepeat.png"))); // NOI18N
        btnRepeatExecution.setToolTipText("Repeated Execution");
        btnRepeatExecution.setFocusable(false);
        btnRepeatExecution.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRepeatExecution.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRepeatExecution.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRepeatExecutionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRepeatExecution);

        btnCancelQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/betadb/gui/icons/cancel.png"))); // NOI18N
        btnCancelQuery.setToolTipText("Cancel");
        btnCancelQuery.setFocusable(false);
        btnCancelQuery.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelQuery.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelQuery.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelQueryActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCancelQuery);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/betadb/gui/icons/disksave.png"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSave);

        btnSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/betadb/gui/icons/disksaveadvanced.png"))); // NOI18N
        btnSaveAs.setToolTipText("Save As");
        btnSaveAs.setFocusable(false);
        btnSaveAs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveAs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveAs.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveAsActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSaveAs);

        btnOpenFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/betadb/gui/icons/folder_page.png"))); // NOI18N
        btnOpenFile.setToolTipText("Open");
        btnOpenFile.setFocusable(false);
        btnOpenFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenFile.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOpenFileActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenFile);

        synchObjectsPane.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/betadb/gui/icons/synch.png"))); // NOI18N
        synchObjectsPane.setFocusable(false);
        synchObjectsPane.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        synchObjectsPane.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        synchObjectsPane.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                synchObjectsPaneActionPerformed(evt);
            }
        });
        jToolBar1.add(synchObjectsPane);

        btnAnalyzeQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/betadb/gui/icons/heirarchy.png"))); // NOI18N
        btnAnalyzeQuery.setFocusable(false);
        btnAnalyzeQuery.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAnalyzeQuery.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAnalyzeQuery.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAnalyzeQueryActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAnalyzeQuery);

        add(jToolBar1);

        pnlMain.setLayout(new javax.swing.BoxLayout(pnlMain, javax.swing.BoxLayout.PAGE_AXIS));
        add(pnlMain);
    }// </editor-fold>//GEN-END:initComponents

	private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExecuteActionPerformed
	{//GEN-HEADEREND:event_btnExecuteActionPerformed
		executeSql(false);
	}//GEN-LAST:event_btnExecuteActionPerformed

	private void btnCancelQueryActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelQueryActionPerformed
	{//GEN-HEADEREND:event_btnCancelQueryActionPerformed
		resultsPanel.cancelQuery();
	}//GEN-LAST:event_btnCancelQueryActionPerformed

	private void btnExecuteAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExecuteAllActionPerformed
	{//GEN-HEADEREND:event_btnExecuteAllActionPerformed
		executeSql(true);
	}//GEN-LAST:event_btnExecuteAllActionPerformed

	private void btnOpenFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOpenFileActionPerformed
	{//GEN-HEADEREND:event_btnOpenFileActionPerformed
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			FileReader reader = null;
			try
			{
				File file = fileChooser.getSelectedFile();
				filePath = file.getPath();				
				BufferedReader buf = new BufferedReader(new FileReader(file));
				StringBuilder builder = new StringBuilder();
				String curLine = buf.readLine();
				while(curLine != null)
				{
					builder.append(curLine);
					builder.append("\n");
					curLine = buf.readLine();
				}
				editorPanel.appendSql(builder.toString());
			}
			catch (Exception ex)
			{
				getLogger(SqlPanel.class.getName()).log(Level.SEVERE, null, ex);
			}
			finally
			{
				try
				{
					reader.close();
				}
				catch (IOException ex)
				{
					getLogger(SqlPanel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}			
		}
	}//GEN-LAST:event_btnOpenFileActionPerformed

	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveActionPerformed
	{//GEN-HEADEREND:event_btnSaveActionPerformed
		save(false);
		
	}//GEN-LAST:event_btnSaveActionPerformed

	private void btnSaveAsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveAsActionPerformed
	{//GEN-HEADEREND:event_btnSaveAsActionPerformed
		save(true);
	}//GEN-LAST:event_btnSaveAsActionPerformed

    private void btnRepeatExecutionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRepeatExecutionActionPerformed
    {//GEN-HEADEREND:event_btnRepeatExecutionActionPerformed
        String inputValue = showInputDialog(btnRepeatExecution,"Seconds between repeats: ", 5);
		int repeatInterval = parseInt(inputValue);
		executeSql(false, repeatInterval);
    }//GEN-LAST:event_btnRepeatExecutionActionPerformed

    private void synchObjectsPaneActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_synchObjectsPaneActionPerformed
    {//GEN-HEADEREND:event_synchObjectsPaneActionPerformed
		String sql = editorPanel.getCurrentWord();
		
    }//GEN-LAST:event_synchObjectsPaneActionPerformed

    private void btnAnalyzeQueryActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAnalyzeQueryActionPerformed
    {//GEN-HEADEREND:event_btnAnalyzeQueryActionPerformed
       
    }//GEN-LAST:event_btnAnalyzeQueryActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnAnalyzeQuery;
    private javax.swing.JButton btnCancelQuery;
    private javax.swing.JButton btnExecute;
    private javax.swing.JButton btnExecuteAll;
    private javax.swing.JButton btnOpenFile;
    private javax.swing.JButton btnRepeatExecution;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveAs;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JButton synchObjectsPane;
    // End of variables declaration//GEN-END:variables
}
