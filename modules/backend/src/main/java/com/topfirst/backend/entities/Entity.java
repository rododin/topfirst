/* 
 * Entity.java
 */

package com.topfirst.backend.entities;

import java.io.Serializable;

/**
 * Introduces an abstract entity of the business model.
 *
 * @author Rod Odin
 */
public interface Entity
	extends Serializable
{
	/**
	 * By project good style rules it is good to provide a generic <code>{@link Long}</code> ID  for every entity
	 * used as primary key in the underlying data source table.
	 * @return the unique ID of the entity
	 */
	public Long getId();
	
	/**
	 * An entity is formally new if it is just created and still not stored in the underlying data storage (say in DB).
	 * If the entity is loaded from the storage, it is not new already.
	 * Thus be default the just created <code>{@link Entity}</code> instance is new,
	 * and the data loading/storing methods should reset it explicitly.
	 * @return <code>true</code> if the entity is new, <code>false</code> otherwise
	 */
	public boolean isNew();

	/**
	 * An entity is modified if it has been modified since last storing/loading operation.
	 * I.e. normally setter methods should set the <code>modified</code> flag.
	 * And the data loading/storing methods should reset it.
	 * @return <code>true</code> if the entity is modified, <code>false</code> otherwise
	 */
	public boolean isModified();
}
