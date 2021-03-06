/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqltemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.swingautocompletion.main.AutoCompleteItem;
import com.swingautocompletion.main.SimpleAutoCompleteItem;
import com.swingautocompletion.util.Pair;
import java.util.Collection;
import java.util.Map;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author parmstrong
 */
public class SqlTemplateDialog extends javax.swing.JDialog
{
    private final Preferences prefs;
    private final Gson gson = new Gson();
    private final SqlTemplateTableModel tableModel;

    /**
     * Creates new form SqlTemplateDialog
     */
    @Inject
    public SqlTemplateDialog(SqlTemplateTableModel tableModel)
    {
        this.tableModel = tableModel;
        initComponents();

        prefs = Preferences.userNodeForPackage(this.getClass());
        tblTemplates.getSelectionModel().addListSelectionListener((ListSelectionEvent lse) ->
        {
            int selectedRow = tblTemplates.getSelectedRow();
            if (selectedRow >= 0)
            {
                Pair<String, String> templateForRow = tableModel.getTemplateForRow(tblTemplates.convertRowIndexToModel(selectedRow));
                txtTemplateName.setText(templateForRow.getFirst());
                txtTemplate.setText(templateForRow.getSecond());
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
        txtTemplateName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtTemplate = new javax.swing.JTextArea();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTemplates = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Sql Template"));

        jLabel1.setText("Name:");

        jLabel2.setText("Template:");

        txtTemplate.setColumns(20);
        txtTemplate.setRows(5);
        jScrollPane2.setViewportView(txtTemplate);

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTemplateName)
                            .addComponent(jScrollPane2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtTemplateName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnDelete)))
        );

        tblTemplates.setModel(tableModel);
        jScrollPane1.setViewportView(tblTemplates);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveActionPerformed
    {//GEN-HEADEREND:event_btnSaveActionPerformed
        saveTemplate();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteActionPerformed
    {//GEN-HEADEREND:event_btnDeleteActionPerformed
        deleteTemplate();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void showMe()
    {
        Map<String, String> savedTemplates = getSavedTemplates();
        tableModel.setData(savedTemplates);
        this.setVisible(true);
    }

    public Collection<? extends AutoCompleteItem> getSavedTemplatesAsAutoCompleteItems()
    {
        Map<String, String> savedTemplates = getSavedTemplates();
        return savedTemplates.entrySet().stream().map(t -> new SimpleAutoCompleteItem(t.getKey(), t.getKey(), t.getValue())).collect(Collectors.toList());
    }

    public void createAsTemplate(String selectedText)
    {
        txtTemplate.setText(selectedText);
        this.showMe();
    }

    private Map<String, String> getSavedTemplates()
    {
        String savedTemplates = prefs.get("savedTemplates", "{}");
        java.lang.reflect.Type collectionType = new TypeToken<Map<String, String>>()
        {
        }.getType();
        return gson.fromJson(savedTemplates, collectionType);
    }

    private void saveTemplates(Map<String, String> templates)
    {
        prefs.put("savedTemplates", gson.toJson(templates));
        tableModel.setData(templates);
        tableModel.fireTableDataChanged();
    }

    private void saveTemplate()
    {
        Map<String, String> savedTemplates = getSavedTemplates();
        savedTemplates.put(txtTemplateName.getText(), txtTemplate.getText());
        saveTemplates(savedTemplates);

    }

    private void deleteTemplate()
    {
        Map<String, String> savedTemplates = getSavedTemplates();
        String name = txtTemplateName.getText();
        savedTemplates.remove(name);
        saveTemplates(savedTemplates);

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblTemplates;
    private javax.swing.JTextArea txtTemplate;
    private javax.swing.JTextField txtTemplateName;
    // End of variables declaration//GEN-END:variables

}
