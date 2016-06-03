package com.betadb.gui.sql.filter;

import java.util.List;
import javax.swing.RowFilter;

/**
 *
 * @author parmstrong
 */
public interface ColumnFilterCallBack
{
	public void applyFilters(List<RowFilter> filters);

}
