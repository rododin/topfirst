/*
 * UserException.java
 */

package com.topfirst.backend.exceptions;

import com.topfirst.backend.entities.User;

/**
 * Thrown if something is wrong with user, e.g. if the client tries to add a user with existing username.
 *
 * @author Rod Odin
 */
public class UserException
	extends EntityException
{
// Constructors --------------------------------------------------------------------------------------------------------

	public UserException(User.UserConstraintViolation constraintViolation)
	{
		super(constraintViolation);
	}

	public UserException(String message, User.UserConstraintViolation constraintViolation)
	{
		super(message, constraintViolation);
	}

	public UserException(String message, Throwable cause, User.UserConstraintViolation constraintViolation)
	{
		super(message, cause, constraintViolation);
	}

	public UserException(Throwable cause, User.UserConstraintViolation constraintViolation)
	{
		super(cause, constraintViolation);
	}

	public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, User.UserConstraintViolation constraintViolation)
	{
		super(message, cause, enableSuppression, writableStackTrace, constraintViolation);
	}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	public User.UserConstraintViolation getConstraintViolation()
	{
		return (User.UserConstraintViolation)super.getConstraintViolation();
	}
}
