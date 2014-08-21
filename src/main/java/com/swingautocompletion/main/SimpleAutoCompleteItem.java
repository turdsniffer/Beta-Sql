package com.swingautocompletion.main;

/*
 * #%L
 * SwingAutoCompletion
 * %%
 * Copyright (C) 2013 - 2014 SwingAutoComplete
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
