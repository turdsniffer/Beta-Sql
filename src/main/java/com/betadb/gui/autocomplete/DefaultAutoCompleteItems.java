package com.betadb.gui.autocomplete;

import com.swingautocompletion.main.AutoCompleteItem;
import com.swingautocompletion.main.SimpleAutoCompleteItem;
import java.util.ArrayList;
import java.util.List;

/**
 * @author parmstrong
 */
public class DefaultAutoCompleteItems
{
	public static List<AutoCompleteItem> getitems()
	{
		List<AutoCompleteItem> items = new ArrayList<AutoCompleteItem>();		
		items.add(new SimpleAutoCompleteItem("ssf", "select * from (alias)", "select * from "));
		items.add(new SimpleAutoCompleteItem("selectt100", "select top 100 * from (alias)", "select top 100 * from "));
		items.add(new SimpleAutoCompleteItem("updateset", "update set (alias)", "update set "));
		items.add(new SimpleAutoCompleteItem("ii", "insert into values () (alias)", "insert into values ()"));
		items.add(new SimpleAutoCompleteItem("insert", "insert into values () (alias)", "insert into values ()"));
		items.add(new SimpleAutoCompleteItem("delete", "Delete from (alias)", "delete from  where"));		
		return items;
	}
}
