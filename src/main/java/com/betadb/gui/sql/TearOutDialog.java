/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TearOutDialog.java
 *
 * Created on Sep 1, 2011, 5:20:41 PM
 */

package com.betadb.gui.sql;

import javax.swing.JTable;

/**
 *
 * @author parmstrong
 */
public class TearOutDialog extends javax.swing.JDialog {

    /** Creates new form TearOutDialog */
    public TearOutDialog(JTable table) 
	{    				
        initComponents();
		this.setLocationRelativeTo(null);
		jScrollPane1.setViewportView(table);	
		jScrollPane1.revalidate();		
    }
	
	

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(600, 400));
        jPanel1.add(jScrollPane1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
