
package com.betadb.gui.connection;

import javax.sql.DataSource;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * @author parmstrong
 */
public abstract class LazyLoadNode extends DefaultMutableTreeNode
{
	protected DataSource dataSource;
	protected DefaultTreeModel treeModel;
	protected final DefaultMutableTreeNode loadingNode;
	private boolean loaded;
    protected JTree jtree;
	

	public LazyLoadNode(Object userObject,  DefaultTreeModel treeModel, JTree treeDbs)
	{
		super(userObject);
		this.loadingNode = new DefaultMutableTreeNode("Loading...");
		this.add(loadingNode);
		this.treeModel = treeModel;
		this.loaded = false;
        this.jtree = treeDbs;        
	}

	public void load()
	{
		load(false);
	}

	public void load(boolean forceRefresh)
	{
		if(loaded && !forceRefresh)
            return;

		performLoadAction();		
		loaded = true;
        this.treeModel.nodeStructureChanged(this);
        this.jtree.expandPath(new TreePath(this.getPath()));
	}

	protected abstract void performLoadAction();
    
}
