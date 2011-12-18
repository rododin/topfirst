/*
 * BannerException.java
 */

package com.topfirst.backend.exceptions;

import com.topfirst.backend.entities.Banner;

/**
 * Thrown when something is wrong with banner on a banner-related operation, e.g. if the client tries to add a banner
 * with existing title.
 *
 * @author Rod Odin
 */
public class BannerException
	extends EntityException
{
// Constructors --------------------------------------------------------------------------------------------------------

	public BannerException(Banner.BannerConstraintViolation constraintViolation)
	{
		super(constraintViolation);
	}

	public BannerException(String message, Banner.BannerConstraintViolation constraintViolation)
	{
		super(message, constraintViolation);
	}

	public BannerException(String message, Throwable cause, Banner.BannerConstraintViolation constraintViolation)
	{
		super(message, cause, constraintViolation);
	}

	public BannerException(Throwable cause, Banner.BannerConstraintViolation constraintViolation)
	{
		super(cause, constraintViolation);
	}

	public BannerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Banner.BannerConstraintViolation constraintViolation)
	{
		super(message, cause, enableSuppression, writableStackTrace, constraintViolation);
	}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	public Banner.BannerConstraintViolation getConstraintViolation()
	{
		return (Banner.BannerConstraintViolation)super.getConstraintViolation();
	}
}
