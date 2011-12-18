/*
 * User.java
 */

package com.topfirst.backend.impl.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.topfirst.backend.entities.User;
import com.topfirst.generic.utils.PasswordSignatureGenerator;
import org.hibernate.annotations.GenericGenerator;

/**
 * Description.
 *
 * @author Rod Odin
 */
@Entity
@Table(name = "users")
public class UserImpl
	extends AbstractEntityImpl
	implements User
{
// Constructors --------------------------------------------------------------------------------------------------------

	/**
	 * Required for Hibernate.
	 */
	public UserImpl()
	{
	}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	@Id
	@Column(name = "user_id", nullable = false, unique = true)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	public Long getId()
	{
		return super.getId();
	}

	@Column(name = "email", nullable = false, unique = true)
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
		setModified(true);
	}

	@Transient
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
		setModified(true);
	}

	@Column(name = "password_signature", nullable = false, length = 32)
	public String getPasswordSignature()
	{
		return passwordSignature;
	}

	public void setPasswordSignature(String passwordSignature)
	{
		this.passwordSignature = passwordSignature;
		setModified(true);
	}

	@Column(name = "first_name", nullable = true, unique = false)
	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
		setModified(true);
	}

	@Column(name = "last_name", nullable = true, unique = false)
	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
		setModified(true);
	}

	@OneToMany(mappedBy = "user")
	public Collection<BannerImpl> getBanners()
	{
		return Collections.unmodifiableList(banners);
	}

	public void setBanners(Collection<BannerImpl> banners)
	{
		this.banners = banners != null ? new ArrayList<BannerImpl>(banners) : new ArrayList<BannerImpl>();
		setModified(true);
	}

	public void addBanner(BannerImpl banner)
	{
		banners.add(banner);
		setModified(true);
	}

	public void removeBanner(BannerImpl banner)
	{
		banners.remove(banner);
		setModified(true);
	}

	@Column(name = "disabled")
	public boolean isDisabled()
	{
		return disabled;
	}

	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
		setModified(true);
	}

	@Transient
	public boolean isLoggedIn()
	{
	  return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn)
	{
	  this.loggedIn = loggedIn;
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private static final long serialVersionUID = 1599247075954774216L;

	private String email;
	private String password;
	private String passwordSignature;
	private String firstName;
	private String lastName;
	private List<BannerImpl> banners = new LinkedList<>();
	private boolean disabled;
	private boolean loggedIn;
}
