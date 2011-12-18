/*
 * User.java
 */

package com.topfirst.backend.entities;

import java.util.Collection;

/**
 * Represents a user account.
 *
 * @author Rod Odin
 */
public interface User
	extends Entity
{
	/**
	 * Introduces business level user violation constraints.
	 */
	public static enum UserConstraintViolation
	{
		IllegalEmailFormat, EmailAlreadyExists, IllegalPasswordFormat,
	}

	public String getEmail();
	public void setEmail(String email);

	public String getPassword();
	public void setPassword(String password);

	public String getFirstName();
	public void setFirstName(String firstName);

	public String getLastName();
	public void setLastName(String lastName);

	/**
	 * Returns unmodifiable collection of the <code>{@link Banner}</code>s owned by the user.
	 * @return non-<code>null</code>, but empty or non-empty collection
	 */
	public Collection<? extends Banner> getBanners();
	//public void setBanners(Collection<? extends Banner> banners);

	public boolean isDisabled();
	//public void setDisabled(boolean disabled);

	public boolean isLoggedIn();
	//public void setLoggedIn(boolean loggedIn);
}
