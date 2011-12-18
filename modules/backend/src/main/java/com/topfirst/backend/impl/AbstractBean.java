/*
 * AbstractBean.java
 */

package com.topfirst.backend.impl;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Introduces a useful base implementation of the <code>{@link Bean}</code> interface.
 *
 * @author Rod Odin
 */
public abstract class AbstractBean
	implements Bean
{
// Constants -----------------------------------------------------------------------------------------------------------

	protected final static Logger LOG = LoggerFactory.getLogger(AbstractBean.class);

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

	private final EntityManager entityManager;
}
