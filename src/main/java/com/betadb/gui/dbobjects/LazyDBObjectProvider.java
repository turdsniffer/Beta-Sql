
package com.betadb.gui.dbobjects;

/**
 * @author parmstrong
 */
public class LazyDBObjectProvider<T>
{
	private T dbObject;
	private DBObjectLoader<T> loader;

	public LazyDBObjectProvider(DBObjectLoader<T> loader)
	{
		this.loader = loader;
	}

	public T get()
	{
		if(dbObject == null)
			dbObject = loader.load();
		return dbObject;
	}
}
