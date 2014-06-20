package com.betadb.gui.events;

import java.util.ArrayList;
import java.util.List;

/**
 * @author parmstrong
 */
public class EventManager
{
	private List<EventListener> listeners;
	private static EventManager eventManager;

	private EventManager()
	{
		listeners = new ArrayList<>();
	}
	
	public static EventManager getInstance()
	{
		if(eventManager == null)
			eventManager = new EventManager();
		return eventManager;
	}
	
	public void fireEvent(Event evt, Object value)
	{
		ArrayList<EventListener> eventListeners = new ArrayList<>(listeners);//make a copy because our firing of events might result in other event listeners being created and added to the list.
		for (EventListener eventListener : eventListeners)
			eventListener.EventOccurred(evt, value);
	}	
	
	public void addEventListener(EventListener listener)
	{
		this.listeners.add(listener);
	}
}
