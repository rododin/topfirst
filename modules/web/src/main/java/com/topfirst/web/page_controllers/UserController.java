/*
 * UserController.java
 */

package com.topfirst.web.page_controllers;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

import com.topfirst.backend.BackEnd;
import com.topfirst.backend.UserManager;
import com.topfirst.backend.entities.User;
import com.topfirst.backend.exceptions.UserException;

/**
 * Description.
 *
 * @author Rod Odin
 */
@ManagedBean (name = "userController")
@SessionScoped
public class UserController
	extends AbstractController
{
// Constants -----------------------------------------------------------------------------------------------------------

	public static final String DEFAULT_RESOURCE_BUNDLE_NAME = UserController.class.getName();

	public static final String MSG_KEY_ERROR_INTERNAL                   = "error.internal";
	public static final String MSG_KEY_ERROR_AUTHENTICATION             = "error.authentication";
	public static final String MSG_KEY_ERROR_PASSWORD_MISMATCH          = "error.password_mismatch";
	public static final String MSG_KEY_ERROR_ILLEGAL_EMAIL_FORMAT       = "error.illegal_email_format";
	public static final String MSG_KEY_ERROR_EMAIL_ALREADY_EXISTS       = "error.email_already_exists";
	public static final String MSG_KEY_ERROR_ILLEGAL_PASSWORD_FORMAT    = "error.illegal_password_format";
	public static final String MSG_KEY_ERROR_OTHER_CONSTRAINT_VIOLATION = "error.other_constraint_violation";
	public static final String MSG_KEY_INFO_USER_CREATED                = "info.user_created";

	public static final String SESSION_ATTRIBUTE_KEY_LOGGED_IN_USER = "logged_in_user";

// Constructors --------------------------------------------------------------------------------------------------------

	public UserController()
	{
		super (DEFAULT_RESOURCE_BUNDLE_NAME);
		reset();
	}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getConfirm()
	{
		return confirm;
	}

	public void setConfirm(String confirm)
	{
		this.confirm = confirm;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
			return lastName;
		}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public boolean isUserLoggedIn()
	{
		final User user = this.user.get();
		return user != null && user.isLoggedIn();
	}

	public String getFirstNameOfLoggedInUser()
	{
		final User user = this.user.get();
		return user != null ? user.getFirstName() : "";
	}

// Actions -------------------------------------------------------------------------------------------------------------

	public String loginAction()
	{
		final User user;
		try
		{
			user = userManager.loginUser(email, password);
		}
		catch (Exception x)
		{
			LOG.error("Login error: email=" + email + ", password=" + password, x);
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage(MSG_KEY_ERROR_INTERNAL), ""));
			return null;
		}

		if (user == null || !user.isLoggedIn())
		{
			LOG.error("Invalid email/password: email=" + email + ", password=" + password);

			if (loginAttemptCounter.incrementAndGet() > 2)
				return "passfailPage?faces-redirect=true";
			else
			{
				getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, formatMessage(MSG_KEY_ERROR_AUTHENTICATION, email), ""));
				return null;
			}
		}

		// The user is successfully logged-in
		{
			LOG.info("User logged in: " + user.getFirstName());

			// Saving the user in this controller for the future use
			this.user.set(user);

			// Saving the user in the session for the further use in other controllers
			final HttpSession session = getSession();
			if(session != null)
				session.setAttribute(SESSION_ATTRIBUTE_KEY_LOGGED_IN_USER, user);
			else
				LOG.error("Session cannot be retrieved: email=" + email + ", password=" + password);

			// Setting loggedIn request parameter to true to get Login Popup Dialog closed by JavaScript (AJAX)
			getRequestContext().addCallbackParam("loggedIn", true);
		}

		return null;
	}

	public String logoutAction()
	{
		final User user = this.user.get();
		if(user != null)
		{
			try
			{
				userManager.logoutUser(user);

			// Removing the user from the session
			final HttpSession session = getSession();
			if(session != null)
				session.removeAttribute(SESSION_ATTRIBUTE_KEY_LOGGED_IN_USER);
			else
				LOG.error("Session cannot be retrieved: email=" + email + ", password=" + password);
			}
			catch (UserException x)
			{
				LOG.error("Logout error: email=" + user.getEmail(), x);
			}
		}

		reset();
		return null;
	}

	public String createUserAction()
	{
		final Set<User.UserConstraintViolation> userConstraintViolations;
		final User newUser = userManager.createDefaultUser();
		newUser.setEmail(email);
		newUser.setPassword(password);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);

		try
		{
			userConstraintViolations = userManager.verifyUser(newUser);
		}
		catch (Exception x)
		{
			LOG.error("User verification error: email=" + email, x);
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage(MSG_KEY_ERROR_INTERNAL), ""));
			return null;
		}

		if(userConstraintViolations == null)
		{
			if(!password.equals(confirm))
			{
				LOG.error("Password mismatch: email=" + email + ", password=" + password + ", confirm=" + confirm);
				getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage(MSG_KEY_ERROR_PASSWORD_MISMATCH), ""));
				return null;
			}
		}
		else
		{
			if(userConstraintViolations.contains(User.UserConstraintViolation.IllegalEmailFormat))
			{
				LOG.error("Illegal email format: email=" + email);
				getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, formatMessage(MSG_KEY_ERROR_ILLEGAL_EMAIL_FORMAT, email), ""));
				return null;
			}

			if(userConstraintViolations.contains(User.UserConstraintViolation.EmailAlreadyExists))
			{
				LOG.error("Email already exists: email=" + email);
				getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, formatMessage(MSG_KEY_ERROR_EMAIL_ALREADY_EXISTS, email), ""));
				return null;
			}

			if(userConstraintViolations.contains(User.UserConstraintViolation.IllegalPasswordFormat))
			{
				LOG.error("Illegal password format: email=" + email + ", password=" + password);
				getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage(MSG_KEY_ERROR_ILLEGAL_PASSWORD_FORMAT), ""));
				return null;
			}

			if(!userConstraintViolations.isEmpty())
			{
				LOG.error("Other constraint violation: email=" + email);
				getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, formatMessage(MSG_KEY_ERROR_OTHER_CONSTRAINT_VIOLATION, userConstraintViolations), ""));
				return null;
			}
		}

		// Saving the user in the DB
		try
		{
			userManager.addOrUpdateUser(newUser);
		}
		catch (Exception x)
		{
			LOG.error("User saving error: email=" + email, x);
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage(MSG_KEY_ERROR_INTERNAL), "User saving error"));
			return null;
		}

		// User is created successfully
		{
			LOG.info("New user created: email=" + email);
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, formatMessage(MSG_KEY_INFO_USER_CREATED, email), ""));

			// Logging the user in
			loginAction();

			reset();
		}

		return"homePage?faces-redirect=true";
	}

// Internal Logic ------------------------------------------------------------------------------------------------------

	private void reset()
	{
		setEmail("");
		setPassword("");
		setConfirm("");
		setLastName("");
		setFirstName("");
		loginAttemptCounter.set(0);
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private final UserManager userManager = BackEnd.getDefaultBackend().getUserManager();

	private String email;
	private String password;
	private String confirm;
	private String firstName;
	private String lastName;

	private final AtomicInteger loginAttemptCounter = new AtomicInteger();

	/** Logged-in user */
	private final AtomicReference<User> user = new AtomicReference<>();
}
