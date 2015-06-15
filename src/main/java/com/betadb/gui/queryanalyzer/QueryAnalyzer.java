package com.betadb.gui.queryanalyzer;

import javax.swing.JPanel;

/**
 *
 * @author parmstrong
 */
public abstract class QueryAnalyzer extends JPanel
{

	public abstract void analyze(String sql);
	
}
