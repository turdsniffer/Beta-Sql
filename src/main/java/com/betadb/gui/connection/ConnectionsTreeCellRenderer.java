package com.betadb.gui.connection;

import com.betadb.gui.dbobjects.Column;
import com.betadb.gui.dbobjects.DbObject;
import com.betadb.gui.icons.Icon;
import java.awt.Component;
import static java.lang.String.valueOf;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author parmstrong
 */
public class ConnectionsTreeCellRenderer extends DefaultTreeCellRenderer
{

	 
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
	{
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		this.setText(value.toString());		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		Object userObject = node.getUserObject();
		ImageIcon icon = Icon.getIcon(userObject.getClass());
		if(icon != null)
			setIcon(icon);
		

		if (userObject instanceof Column)
		{
			Column col = (Column)userObject;
			setText(col.getName()+" "+col.getDataType()+"("+valueOf(col.getDecimalDigits())+")");
		}
		else if(userObject instanceof DbObject)
		{
			DbObject obj = (DbObject)userObject;
            String schema = obj.getSchemaName() == null ? "":obj.getSchemaName()+".";
			setText(schema+obj.getName());
		}
		
		return this;
	}	
}
