/*
 * AbstractBean.java
 */

package com.topfirst.backend.beans;

import javax.persistence.EntityManager;

/**
 * Not a real EJB3 bean, but a primitive substitute.
 *
 * @author Rod Odin
 */
public abstract class AbstractBean <Entity>
	implements Bean<Entity>
{
// Constructors --------------------------------------------------------------------------------------------------------

	protected AbstractBean(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	public EntityManager getEntityManager()
	{
		return entityManager;
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private EntityManager entityManager;
}
