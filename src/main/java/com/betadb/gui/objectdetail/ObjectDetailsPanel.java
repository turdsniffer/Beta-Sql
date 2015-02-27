package com.betadb.gui.objectdetail;

import com.betadb.gui.dbobjects.DbObject;
import com.betadb.gui.events.Event;
import com.betadb.gui.events.EventListener;
import com.betadb.gui.events.EventManager;
import com.betadb.gui.table.util.ZebraTableRenderer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author parmstrong
 */
@Singleton
public class ObjectDetailsPanel extends javax.swing.JPanel implements EventListener
{
	DefaultTableModel model;
	
    /** Creates new form NewJPanel */
	@Inject
    public ObjectDetailsPanel(EventManager eventManager) {
        initComponents();
		model = new DefaultTableModel();
		model.addColumn("Name");
		model.addColumn("Value");
		tblDbObjectDetails.setModel(model);
		eventManager.addEventListener(this);
		tblDbObjectDetails.setDefaultRenderer(Object.class, new ZebraTableRenderer());
    }
	
	public void update(DbObject object)
	{
		model.getDataVector().removeAllElements();
		Map<String, String> properties = object.getProperties();
		for (Entry<String,String> property : properties.entrySet())		
			model.addRow(new Object[]{property.getKey(),property.getValue()});	
		model.fireTableDataChanged();
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblDbObjectDetails = new javax.swing.JTable();

        tblDbObjectDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblDbObjectDetails);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDbObjectDetails;
    // End of variables declaration//GEN-END:variables

	@Override
	public void EventOccurred(Event event, Object value)
	{
		if (event == Event.DB_OBJECT_SELECTED)
		{
			DbObject object = (DbObject) value;
			update(object);
		}
	}
}
