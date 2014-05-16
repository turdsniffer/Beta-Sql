
package com.swingautocompletion.main;

import javax.swing.text.JTextComponent;

/**
 * @author parmstrong
 */
public interface SearchTermProvider
{
	public String getSearchTerm(JTextComponent textComponent);
}
