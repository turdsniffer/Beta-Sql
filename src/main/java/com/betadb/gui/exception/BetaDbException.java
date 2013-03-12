
package com.betadb.gui.exception;

/**
 * @author parmstrong
 */
public class BetaDbException extends RuntimeException
{
	public BetaDbException()
	{
	}

	public BetaDbException(String string)
	{
		super(string);
	}

	public BetaDbException(String string, Throwable thrwbl)
	{
		super(string, thrwbl);
	}

}
