/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
