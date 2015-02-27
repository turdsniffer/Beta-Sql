
package com.betadb.gui.script;

import com.betadb.gui.dbobjects.Column;
import com.betadb.gui.dbobjects.PrimaryKey;
import com.betadb.gui.dbobjects.Table;
import java.util.List;

/**
 * @author parmstrong
 */
public class ScriptUtils
{
	public static String scriptTable(Table table)
	{
		List<PrimaryKey> primaryKeys = table.getPrimaryKeys();
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE ");
		sql.append(table.getSchemaName()+"."+table.getName()+"\n(\n");
		for (Column column : table.getColumns())
		{
			sql.append(column.getName());
			sql.append(" ");
			sql.append(column.getDataType());
			if(primaryKeys.size() == 1 && primaryKeys.get(0).getName() == column.getName())
				sql.append(" PRIMARY KEY");
			sql.append(",\n");
		}
		if(primaryKeys != null && primaryKeys.size()>1)
		{
			sql.append("CONSTRAINT pk_"+table.getName()+" PRIMARY KEY (");
			for (PrimaryKey primaryKey : primaryKeys)
			{
				sql.append(primaryKey.getName()).append(",");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
			sql.append(")");
		}

		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(")");
		return sql.toString();
	}
}
