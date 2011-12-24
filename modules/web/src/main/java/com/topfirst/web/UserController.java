/* 
 * UserController.java
 */

package com.topfirst.web;

import javax.faces.bean.ManagedBean;

import com.topfirst.backend.BackEnd;
import com.topfirst.backend.UserManager;
import com.topfirst.backend.entities.User;
import com.topfirst.backend.exceptions.UserException;
import com.topfirst.generic.utils.PasswordSignatureGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description.
 *
 * @author Rod Odin
 */
@ManagedBean (name = "userBean")
public class UserController
{
	final static Logger LOG = LoggerFactory.getLogger(UserController.class);
	public UserController()
	{
	}
	// getters/setters ------------------------------------------------
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password=password;
	}
	public String getConfirm()
	{
		return confirm;
	}
	public void setConfirm(String confirm)
	{
		this.confirm=confirm;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.firstName=lastName;
	}
	public boolean getAuthResult()
	{
		return false;
	};
	//  action --------------------------------------------------------
	public String loginAction()
	{
		LOG.info("loginAction trace --------------");
		String sig= PasswordSignatureGenerator.createSignature(email, password);
		LOG.info("email='"+email+"' password='"+password+"' sig='"+sig+"'");
		try
		{
			user=userManager.loginUser(email,password);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
	public void logoutAction()
	{
		if(user != null)
		{
			try
			{
				userManager.logoutUser(user);
			}
			catch (UserException e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
	}
	// attributes -----------------------------------------------------
	private String email;
	private String password;
	private String confirm;
	private String firstName;
	private String lastName;
	
	private UserManager userManager = BackEnd.getDefaultBackend().getUserManager();
	private User user;
}

