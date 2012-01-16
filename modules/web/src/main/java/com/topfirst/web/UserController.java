/*
 * UserController.java
 */

package com.topfirst.web;

import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.topfirst.backend.BackEnd;
import com.topfirst.backend.UserManager;
import com.topfirst.backend.entities.User;
import com.topfirst.backend.exceptions.PersistenceException;
import com.topfirst.backend.exceptions.UserException;
import org.primefaces.context.RequestContext;
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
	public String getFirstNameOfLoggedInUser()
	{
		return user != null ? user.getFirstName() : "";
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
		this.lastName=lastName;
	}
	public boolean getAuthResult()
	{
		return user != null && user.isLoggedIn();
	}
	//  action --------------------------------------------------------
	public String loginAction()
	{
		try
		{
			user=userManager.loginUser(email,password);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(),e);
		}
		if((user == null)||(!user.isLoggedIn()))
		{
			tryCount++;
			if(tryCount > 2)
			{
				return "passfailPage?faces-redirect=true";
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User doesn't exist: " + email, ""));
			return null;
		}
		//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome "+user.getFirstName(), ""));
		RequestContext.getCurrentInstance().addCallbackParam("loggedIn", user.isLoggedIn());
		LOG.info("User logged in: "+user.getFirstName());
		return null;
	}
	public void logoutAction()
	{
		setEmail("");
		setPassword("");
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
	public String createUserAction()
	{
		final Set<User.UserConstraintViolation> userConstraintViolations;
		final User newUser=userManager.createDefaultUser();
		newUser.setEmail(email);
		newUser.setPassword(password);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);

		try
		{
			userConstraintViolations=userManager.verifyUser(newUser);
		}
		catch (PersistenceException e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "PersistenceException"));
			return null;
		}

		if(userConstraintViolations==null)
		{
			if(!password.equals(confirm))
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password and confirmation do not match", ""));
				return null;
			}
		}
		else
		{
			if(userConstraintViolations.contains(User.UserConstraintViolation.IllegalEmailFormat))
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Illegal email format", ""));
			}
			else
			{
				if(userConstraintViolations.contains(User.UserConstraintViolation.EmailAlreadyExists))
				{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User already exists: "+email, ""));
				}
			}
			if(userConstraintViolations.contains(User.UserConstraintViolation.IllegalPasswordFormat))
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Illegal password format", ""));
			}
			if(!userConstraintViolations.isEmpty())
			{
				return null;
			}
		}

		try
		{
			userManager.addOrUpdateUser(newUser);
		}
		catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "Internal error: Exception in addOrUpdateUser"));
			return null;
		}

		LOG.info("New user created: email="+email);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User account has been created successfully: " + email, ""));
		loginAction();
		resetAttributes();
		return"homePage?faces-redirect=true";
	}
	private void resetAttributes()
	{
		setEmail("");
		setPassword("");
		setConfirm("");
		setLastName("");
		setFirstName("");
	}
	// attributes -----------------------------------------------------
	private String email;
	private String password;
	private String confirm;
	private String firstName;
	private String lastName;

	private int tryCount=0;
	private final UserManager userManager = BackEnd.getDefaultBackend().getUserManager();
	private User user;
}

