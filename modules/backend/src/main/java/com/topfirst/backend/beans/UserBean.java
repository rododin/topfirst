/*
 * UserBean.java
 */

package com.topfirst.backend.beans;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

import com.topfirst.backend.entities.User;

/**
 * TODO: Add other business logic.
 *
 * @author Rod Odin
 */
public class UserBean extends AbstractBean<User>
{
// Constructors --------------------------------------------------------------------------------------------------------

	public UserBean(EntityManager entityManager)
	{
		super(entityManager);
	}

// Bean Implementation -------------------------------------------------------------------------------------------------

	public User create()
	{
		return new User();
	}

	public User save(User user)
	{
		final EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.getTransaction().commit();
		return user;
	}

	public void delete(User user)
	{
		final EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(user);
		entityManager.getTransaction().commit();
	}

	public List<User> get(Object... criteria)
	{
		// TODO: Implement criteria support
		final EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();
		final List<User> rv = entityManager.createQuery("from User", User.class).getResultList();
		entityManager.getTransaction().commit();
		return rv;
	}

	public Map<String, User> getAllUsersMappedByUsername()
	{
		final LinkedHashMap<String, User> rv = new LinkedHashMap<String, User>();
		final List<User> users = get();
		for (User user : users)
			rv.put(user.getUsername(), user);
		return rv;
	}
}
