/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.betadb.gui.connection;

import com.betadb.gui.autocomplete.BetaDbPopupListCellRenderer;
import com.betadb.gui.dbobjects.DbInfo;
import static com.betadb.gui.events.Event.*;
import com.betadb.gui.events.EventManager;
import com.swingautocompletion.main.AutoCompleteHandler;
import com.swingautocompletion.main.AutoCompleteItem;
import com.swingautocompletion.main.AutoCompletePopup;
import com.swingautocompletion.main.SubSuggestionsWordSearchProvider;


/**
 *
 * @author parmstrong
 */
public class FindObjectDialog extends javax.swing.JDialog
{
	ConnectionsPanel connectionsPanel;
	DbInfo dbInfo;
	final AutoCompletePopup autoCompletePopup;
	/**
	 * Creates new form FindObjectDialog
	 */
	public FindObjectDialog(DbInfo dbInfo)
	{
		initComponents();
		this.setTitle("Find Object");
		this.dbInfo = dbInfo;
		connectionsPanel = ConnectionsPanel.getInstance();
		autoCompletePopup = new AutoCompletePopup(txtSearch, new BetaDbPopupListCellRenderer(), new SubSuggestionsWordSearchProvider());
		autoCompletePopup.setAutoCompletePossibilties(dbInfo.getAllDbObjects());
		autoCompletePopup.addAutoCompleteHandler(new AutoCompleteHandler() {

			@Override
			public void handle(AutoCompleteItem autoCompleteItem)
			{
				EventManager.getInstance().fireEvent(DB_OBJECT_SELECTED, autoCompleteItem);
				FindObjectDialog.this.setVisible(false);
			}
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        txtSearch = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSearch)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
