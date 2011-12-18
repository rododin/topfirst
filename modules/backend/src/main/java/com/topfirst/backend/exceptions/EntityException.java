/*
 * EntotyException.java
 */

package com.topfirst.backend.exceptions;

/**
 * Description.
 *
 * @author Rod Odin
 */
public abstract class EntityException
	extends Exception
{
// Constructors --------------------------------------------------------------------------------------------------------

	protected EntityException(Object constraintViolation)
	{
		this.constraintViolation = constraintViolation;
	}

	protected EntityException(String message, Object constraintViolation)
	{
		super(message);
		this.constraintViolation = constraintViolation;
	}

	protected EntityException(String message, Throwable cause, Object constraintViolation)
	{
		super(message, cause);
		this.constraintViolation = constraintViolation;
	}

	protected EntityException(Throwable cause, Object constraintViolation)
	{
		super(cause);
		this.constraintViolation = constraintViolation;
	}

	protected EntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object constraintViolation)
	{
		super(message, cause, enableSuppression, writableStackTrace);
		this.constraintViolation = constraintViolation;
	}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	protected Object getConstraintViolation()
	{
		return constraintViolation;
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private final Object constraintViolation;
}
