package com.betadb.gui.table.util.renderer;

import java.awt.Component;

public interface CellClassRenderer<T>
{
	public Component render(T t, Component c);
}
