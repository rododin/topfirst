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
import com.topfirst.generic.utils.PasswordSignatureGenerator;
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with input email and password does not exist", ""));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Wellcome "+user.getFirstName(), ""));
		RequestContext.getCurrentInstance().addCallbackParam("loggedIn", user.isLoggedIn());
		LOG.info("User logged in as "+user.getFirstName());
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
		Set<User.UserConstraintViolation> userConstraintViolations;
		User newUser=userManager.createDefaultUser();
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
		}else{
			if(userConstraintViolations.contains(User.UserConstraintViolation.IllegalEmailFormat))
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Illegal email format", ""));
			}else{
				if(userConstraintViolations.contains(User.UserConstraintViolation.EmailAlreadyExists))
				{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with email <"+email+"> already exists", ""));
				};
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "exception in addOrUpdateUser"));
			return null;
		}
		LOG.info("Created new user with email ="+email);
		resetAttributes();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "The account was created successfully", ""));
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
	private UserManager userManager = BackEnd.getDefaultBackend().getUserManager();
	private User user;
}

