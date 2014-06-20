
package com.betadb.gui.connection;

import javax.sql.DataSource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author parmstrong
 */
public abstract class LazyLoadNode extends DefaultMutableTreeNode
{
	protected DataSource dataSource;
	protected DefaultTreeModel treeModel;
	protected final DefaultMutableTreeNode loadingNode;
	private boolean loaded;
	

	public LazyLoadNode(Object userObject, DataSource datasource, DefaultTreeModel treeModel)
	{
		super(userObject);
		this.loadingNode = new DefaultMutableTreeNode("Loading...");
		this.add(loadingNode);
		this.dataSource = datasource;
		this.treeModel = treeModel;
		this.loaded = false;
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
	}

	protected abstract void performLoadAction();

}
