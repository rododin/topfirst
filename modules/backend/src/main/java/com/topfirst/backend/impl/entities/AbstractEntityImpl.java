/*
 * AbstractEntityImpl.java
 */

package com.topfirst.backend.impl.entities;

import java.util.concurrent.atomic.AtomicBoolean;

import com.topfirst.backend.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides default base implementation of the <code>{@link Entity}</code> interface.
 *
 * @author Rod Odin
 */
public abstract class AbstractEntityImpl
	implements Entity
{
// Constants -----------------------------------------------------------------------------------------------------------

	protected final static Logger LOG = LoggerFactory.getLogger(AbstractEntityImpl.class);

// Constructors --------------------------------------------------------------------------------------------------------

// Getters/Setters -----------------------------------------------------------------------------------------------------

	public Long getId()
	{
		return id;
	}

	protected void setId(Long id)
	{
		this.id = id;
		setModified(true);
	}

	public boolean isNew()
	{
		return isNew.get();
	}

	public void setNew(boolean isNew)
	{
		this.isNew.set(isNew);
	}

	public boolean isModified()
	{
		return isModified.get();
	}

	public void setModified(boolean isModified)
	{
		this.isModified.set(isModified);
	}

// Overridden Methods --------------------------------------------------------------------------------------------------

	/** Auto-generated */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final AbstractEntityImpl that = (AbstractEntityImpl) o;
		return !(id != null ? !id.equals(that.id) : that.id != null);
	}

	/** Auto-generated */
	public int hashCode()
	{
		return id != null ? id.hashCode() : 0;
	}

	public String toString()
	{
		final StringBuilder rv = new StringBuilder(getClass().getSimpleName());
		rv.append("{id=").append(id);
		rv.append(", isNew=").append(isNew);
		rv.append(", isModified=").append(isModified);
		rv.append('}');
		return rv.toString();
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private volatile Long id;
	private final AtomicBoolean isNew = new AtomicBoolean(true);
	private final AtomicBoolean isModified = new AtomicBoolean(true);
}
