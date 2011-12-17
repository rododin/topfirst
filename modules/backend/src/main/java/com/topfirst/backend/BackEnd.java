/*
 * BackEnd.java
 */

package com.topfirst.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.topfirst.backend.beans.BannerBean;
import com.topfirst.backend.beans.Bean;
import com.topfirst.backend.beans.UserBean;
import com.topfirst.backend.entities.Banner;
import com.topfirst.backend.entities.User;

/**
 * The BackEnd singleton.
 *
 * @author Rod Odin
 */
public class BackEnd
{
// Constructors --------------------------------------------------------------------------------------------------------

	protected BackEnd()
	{
		entityManagerFactory = Persistence.createEntityManagerFactory("com.topfirst.backend.jpa");
		defaultEntityManager = entityManagerFactory.createEntityManager();

		beans.put(User  .class.getName(), new   UserBean(defaultEntityManager));
		beans.put(Banner.class.getName(), new BannerBean(defaultEntityManager));
		//...
	}

// Initialization/Finalization -----------------------------------------------------------------------------------------

	protected void finalize() throws Throwable
	{
		defaultEntityManager.close();
		entityManagerFactory.close();
		super.finalize();
	}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	public static BackEnd getDefaultBackend()
	{
		if (DEFAULT_BACKEND.get() == null)
			DEFAULT_BACKEND.compareAndSet(null, new BackEnd());
		return DEFAULT_BACKEND.get();
	}

	@SuppressWarnings("unchecked")
	public <Entity> Bean<Entity> getBean(Class<Entity> entityClass)
	{
		return (Bean<Entity>)beans.get(entityClass.getName());
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private static final AtomicReference<BackEnd> DEFAULT_BACKEND = new AtomicReference<BackEnd>();

	private EntityManagerFactory entityManagerFactory;
	private EntityManager defaultEntityManager;

	private Map<String, Bean<?>> beans = new HashMap<String, Bean<?>>();
}
