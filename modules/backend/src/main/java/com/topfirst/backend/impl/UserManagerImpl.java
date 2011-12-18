/*
 * UserBean.java
 */

package com.topfirst.backend.impl;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import com.topfirst.backend.BackEnd;
import com.topfirst.backend.UserManager;
import com.topfirst.backend.entities.User;
import com.topfirst.backend.exceptions.PersistenceException;
import com.topfirst.backend.exceptions.UserException;
import com.topfirst.backend.impl.entities.UserImpl;
import com.topfirst.generic.utils.PasswordSignatureGenerator;
import org.apache.commons.validator.EmailValidator;

/**
 * Implements the <code>{@link UserManager}</code> interface providing all necessary functionality.
 * It is just marked abstract to prevent non-managed way of the instance creation.
 *
 * @see BackEnd#getUserManager()
 *
 * @author Rod Odin
 */
public abstract class UserManagerImpl
	extends AbstractBean
	implements UserManager
{
// Constructors --------------------------------------------------------------------------------------------------------

	protected UserManagerImpl(EntityManager entityManager)
	{
		super(entityManager);
	}

// Bean Implementation -------------------------------------------------------------------------------------------------

	public boolean isUserExisting(String email) throws PersistenceException
	{
		return getUser(email) != null;
	}

	public UserImpl getUser(String email) throws PersistenceException
	{
		try
		{
			if (email == null)
				throw new NullPointerException("email is null");
			final EntityManager entityManager = getEntityManager();
			entityManager.getTransaction().begin();
			final UserImpl rv = entityManager.createQuery("select u from UserImpl as u where u.email = ?1", UserImpl.class).setParameter(1, email).getSingleResult();
			entityManager.getTransaction().commit();
			if (rv != null)
			{
				rv.setNew(false);
				rv.setModified(false);
			}
			return rv;
		}
		catch (Exception x)
		{
			final String msg = "Unable to get User by email: email=" + email;
			LOG.error(msg, x);
			throw new PersistenceException(msg, x);
		}
	}

	public UserImpl createDefaultUser()
	{
		final UserImpl rv = new UserImpl();
		//rv.set...
		return rv;
	}

	public Set<User.UserConstraintViolation> verifyUser(User user) throws PersistenceException
	{
		final Set<User.UserConstraintViolation> rv = new HashSet<>();
		if (user.getEmail() == null || !EmailValidator.getInstance().isValid(user.getEmail()))
			rv.add(User.UserConstraintViolation.IllegalEmailFormat);
		if (isUserExisting(user.getEmail()))
			rv.add(User.UserConstraintViolation.EmailAlreadyExists);
		if (user.getPassword() == null || user.getPassword().isEmpty())
			rv.add(User.UserConstraintViolation.IllegalPasswordFormat);
		return rv.isEmpty() ? null : rv;
	}

	public void addOrUpdateUser(User user) throws UserException, PersistenceException
	{
		if (user.isNew() || user.isModified())
		{
			synchronized (user)
			{
				final Set<User.UserConstraintViolation> constrainViolations = verifyUser(user);
				if (constrainViolations != null)
				{
					final String msg = "Unable to add/update user: user=" + user + ", constrainViolations=" + constrainViolations;
					LOG.error(msg);
					throw new UserException(msg, constrainViolations.iterator().next());
				}
				try
				{
					final UserImpl userImpl = (UserImpl) user;
					userImpl.setPasswordSignature(PasswordSignatureGenerator.createSignature(userImpl.getEmail(), userImpl.getPassword()));

					final EntityManager entityManager = getEntityManager();
					entityManager.getTransaction().begin();
					if (user.isNew())
						entityManager.persist(user);
					else
						entityManager.merge(user);

					userImpl.setNew(false);
					userImpl.setModified(false);
				}
				catch (EntityExistsException x)
				{
					final String msg = "Unable to add/update user: user=" + user + ", constrainViolations=" + User.UserConstraintViolation.EmailAlreadyExists;
					LOG.error(msg, x);
					throw new UserException(msg, User.UserConstraintViolation.EmailAlreadyExists);
				}
				catch (Exception x)
				{
					final String msg = "Unable to add/update user: user=" + user;
					LOG.error(msg, x);
					throw new PersistenceException(msg, x);
				}
			}
		}
	}

	public UserImpl loginUser(String email, String password) throws PersistenceException
	{
		UserImpl user = getUser(email);
		if (user != null)
		{
			if (PasswordSignatureGenerator.createSignature(email, password).equals(user.getPasswordSignature()))
				user.setLoggedIn(true);
			else
				user = null;
		}
		return user;
	}

	public void logoutUser(User user) throws UserException
	{
		((UserImpl)user).setLoggedIn(false);
	}

	public void disableUser(String email) throws PersistenceException
	{
		doSetDisabled(email, true);
	}

	public void enableUser(String email) throws PersistenceException
	{
		doSetDisabled(email, false);
	}

// Internal Logic ------------------------------------------------------------------------------------------------------

	protected void doSetDisabled(String email, boolean disabled) throws PersistenceException
	{
		UserImpl user = getUser(email);
		if (user != null)
		{
			try
			{
				synchronized (user)
				{
					user.setDisabled(disabled);
					final EntityManager entityManager = getEntityManager();
					entityManager.getTransaction().begin();
					entityManager.merge(user);
					entityManager.getTransaction().commit();
					user.setNew(false);
					user.setModified(false);
				}
			}
			catch (Exception x)
			{
				final String msg = "Unable to disable/enable user: user=" + user + ", disabled=" + disabled;
				LOG.error(msg, x);
				throw new PersistenceException(msg, x);
			}
		}
	}

	// TODO: Check here if we need an LRU cache of users? 30K users?
}
