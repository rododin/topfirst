/*
 * PersistenceException.java
 */

package com.topfirst.backend.exceptions;

/**
 * A wrapper for any persistence exceptions produced by Hibernate (or another persistence engine if any)
 * on some operation execution, e.g. on DB error.
 * Please note, we force the clients to handle that explicitly, so it extends <code>{@link Exception}</code>
 * instead of <code>{@link RuntimeException}</code>
 *
 * @author Rod Odin
 */
public class PersistenceException
	extends Exception
{
	public PersistenceException()
	{
	}

	public PersistenceException(String message)
	{
		super(message);
	}

	public PersistenceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PersistenceException(Throwable cause)
	{
		super(cause);
	}

	public PersistenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
