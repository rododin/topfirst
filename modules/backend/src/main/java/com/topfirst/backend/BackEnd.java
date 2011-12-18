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

import com.topfirst.backend.impl.BannerManagerImpl;
import com.topfirst.backend.impl.Bean;
import com.topfirst.backend.impl.UserManagerImpl;

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

		beans.put(UserManager  .class.getName(), new UserManagerImpl  (defaultEntityManager) {});
		beans.put(BannerManager.class.getName(), new BannerManagerImpl(defaultEntityManager) {});
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

	public UserManager getUserManager()
	{
		return (UserManager)beans.get(UserManager.class.getName());
	}

	public BannerManager getBannerManager()
	{
		return (BannerManager)beans.get(BannerManager.class.getName());
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private static final AtomicReference<BackEnd> DEFAULT_BACKEND = new AtomicReference<BackEnd>();

	private EntityManagerFactory entityManagerFactory;
	private EntityManager defaultEntityManager;

	private Map<String, Bean> beans = new HashMap<>();
}
