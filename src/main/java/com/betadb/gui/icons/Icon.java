package com.betadb.gui.icons;

import com.betadb.gui.dbobjects.Column;
import com.betadb.gui.dbobjects.DbInfo;
import com.betadb.gui.dbobjects.Function;
import com.betadb.gui.dbobjects.Index;
import com.betadb.gui.dbobjects.Parameter;
import com.betadb.gui.dbobjects.Procedure;
import com.betadb.gui.dbobjects.Table;
import com.betadb.gui.dbobjects.View;
import javax.swing.ImageIcon;


/**
 *
 * @author parmstrong
 */
public enum Icon
{	
	COLUMN("column.png", Column.class),
	TABLE("table.png", Table.class),
	FUNCTION("function.png", Function.class),
	VIEW("view.png", View.class),
	PROCEDURE("procedure.png", Procedure.class),
	PARAMETER("parameter.png", Parameter.class),
	INDEX("index.png", Index.class),
	DATABASE("database.png", DbInfo.class);
	
	private static String iconBaseFilePath = "/com/betadb/gui/icons/";
	String fileName;
	Class iconClass;

	private Icon(String fileName, Class iconClass)
	{
		this.fileName = fileName;
		this.iconClass = iconClass;
	}
	
	public ImageIcon getIcon()
	{
		return new ImageIcon(this.getClass().getResource(iconBaseFilePath+fileName));
	}
	
	public static ImageIcon getIcon(Class c)
	{
		for (Icon icon : Icon.values())
		{
			if(icon.iconClass.equals(c))
				return icon.getIcon();
		}
		return null;
	}	
	
	public static ImageIcon getIcon( String fileName )
	{
		return new ImageIcon(Icon.class.getResource(iconBaseFilePath+fileName));
	}
}
