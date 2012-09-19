package com.swingautocompletion.main;

/**
 * @author parmstrong
 */
public class SimpleAutoCompleteItem extends AutoCompleteItem
{
	private final String autoCompleteId;
	private final String description;
	private final String autoCompletion;

	public SimpleAutoCompleteItem(String autoCompleteId)
	{
		this.autoCompleteId = autoCompleteId;
		this.description = "";
		this.autoCompletion = "";
	}

	public SimpleAutoCompleteItem(String autoCompleteId, String description, String autoCompletion)
	{
		this.autoCompleteId = autoCompleteId;
		this.description = description;
		this.autoCompletion = autoCompletion;
	}	
	
	@Override
	public String getAutoCompleteId()
	{
		return autoCompleteId;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public String getAutoCompletion()
	{
		return autoCompletion;
	}
	
}
