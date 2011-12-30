/* 
 * UserController.java
 */

package com.topfirst.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.topfirst.backend.BackEnd;
import com.topfirst.backend.UserManager;
import com.topfirst.backend.entities.User;
import com.topfirst.backend.exceptions.PersistenceException;
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
@SessionScoped
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
		return (user == null)?firstName:user.getFirstName();
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
		return (user == null)?false:user.isLoggedIn();
	};
	//  action --------------------------------------------------------
	public String loginAction()
	{
		LOG.info("loginAction trace 001 -------------- tryCount="+tryCount);
		try
		{
			user=userManager.loginUser(email,password);
			LOG.info("authres"+user.isLoggedIn());
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
		}
		if((user == null)||(!user.isLoggedIn()))
		{
			tryCount++;
			LOG.info("--tryCount-"+tryCount);
			if(tryCount > 2)
			{
				LOG.info("--redirect tryCount-"+tryCount);
				return "passfailPage?faces-redirect=true";
			}
		};
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
	public String goAction1()
	{
		LOG.info("-- goAction1 --");
		return "page1?faces-redirect=true";
	};
	public String goAction2()
	{
		LOG.info("-- goAction2 --");
		return "passfailPage?faces-redirect=true";
	};
	// attributes -----------------------------------------------------
	private String email;
	private String password;
	private String confirm;
	private String firstName;
	private String lastName;
	
	private int tryCount=0;
	private UserManager userManager = BackEnd.getDefaultBackend().getUserManager();
	private User user;
}

