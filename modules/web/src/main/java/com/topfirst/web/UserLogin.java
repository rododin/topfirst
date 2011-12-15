package com.topfirst.web;

import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Vofka
 * Date: 12.12.11
 * Time: 1:56
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "UserLogin")
public class UserLogin
{
	final static Logger LOG = LoggerFactory.getLogger(UserLogin.class);

	// getter / setter ------------------------------------

	public void setUsername(String username)
	{
		this.username=username;
	}

	public String getUsername()
	{
		return username;
	}

	public void setPassword(String password)
	{
		this.password=password;
	}

	public String getPassword()
	{
		return password;
	}

	public void setAuthResult(boolean authResult)
	{
		this.authResult=authResult;
	}

	public boolean getAuthResult()
	{
		return authResult;
	}

// bottom listener ------------------------------------


	public void loginBottomPressed()
	{
		authResult = (username.equals("q")) && (password.equals("q"));
		LOG.info("user auth reasult - " + authResult);
	}

	public void logoutBottomPressed()
	{
		authResult=false;
	}

	// attributes -----------------------------------------

	private String username = "";
	private String password = "";
	private boolean authResult=false;
}
