/* 
 * AccountCreatorController.java
 */

package com.topfirst.web;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description.
 *
 * @author Rod Odin
 */
@ManagedBean(name = "createAccountBean")
public class AccountCreatorController
{
	final static Logger LOG = LoggerFactory.getLogger(AccountCreatorController.class);
	public AccountCreatorController()
	{
	}
// setters / getters ----------------------------------
	public void setEmail(String email)
	{
		this.email=email;
	};
	public String getEmail()
	{
		return email;
	}
	public void setPassword(String password)
	{
		this.password=password;
	}
	public String getPassword()
	{
		return password;
	}
	public void setConfirm(String confirm)
	{
		this.confirm=confirm;
	}
	public String getConfirm()
	{
		return confirm;
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public String getLastName()
	{
		return lastName;
	}
// bottom listener ------------------------------------
	public void createAccountPressed(ActionEvent actionEvent)
	{
		LOG.info("user account create");
		FacesContext facesContext=FacesContext.getCurrentInstance();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Added message",null );
		facesContext.addMessage("null", msg);
	}
// attributes -----------------------------------------
	private String email;
	private String password;
	private String confirm;
	private String firstName;
	private String lastName;
}

