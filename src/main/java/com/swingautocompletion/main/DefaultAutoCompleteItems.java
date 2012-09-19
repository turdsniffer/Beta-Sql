package com.swingautocompletion.main;

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
		items.add(new SimpleAutoCompleteItem("st100f", "select top 100 * from (alias)", "select top 100 * from "));
		items.add(new SimpleAutoCompleteItem("updateset", "update set (alias)", "update set "));
		items.add(new SimpleAutoCompleteItem("ii", "insert into values () (alias)", "insert into values ()"));
		
		return items;
	}
}
