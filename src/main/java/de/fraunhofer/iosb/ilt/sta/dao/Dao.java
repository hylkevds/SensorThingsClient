package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.query.Expansion;
import de.fraunhofer.iosb.ilt.sta.query.Query;
import java.net.URI;

/**
 * CRUD operations for data access objects (Daos).
 *
 * @author Nils Sommer
 *
 * @param <T> the entity's type
 */
public interface Dao<T extends Entity> {

	/**
	 * Create a new entity.
	 *
	 * @param entity the entity to create
	 * @throws ServiceFailureException the operation failed
	 */
	void create(T entity) throws ServiceFailureException;

	/**
	 * Find an entity.
	 *
	 * @param id the entity's unique id
	 * @return the entity
	 * @throws ServiceFailureException the operation failed
	 */
	T find(Long id) throws ServiceFailureException;

	/**
	 * Find an entity.
	 *
	 * @param uri the entity's URI
	 * @return the entity
	 * @throws ServiceFailureException the operation failed
	 */
	T find(URI uri) throws ServiceFailureException;

	/**
	 * Find an entity including referenced entities from expansion.
	 *
	 * @param id the entity's unique id
	 * @param expansion the expansion containing which referenced entities to
	 * fetch
	 * @return the entity
	 * @throws ServiceFailureException the operation failed
	 */
	T find(Long id, Expansion expansion) throws ServiceFailureException;

	/**
	 * Update an entity.
	 *
	 * @param entity the entity to update
	 * @throws ServiceFailureException the operation failed
	 */
	void update(T entity) throws ServiceFailureException;

	/**
	 * Delete an entity.
	 *
	 * @param entity the entity to delete
	 * @throws ServiceFailureException the operation failed
	 */
	void delete(T entity) throws ServiceFailureException;

	/**
	 * Start a query to find an entity collection.
	 *
	 * @return the query
	 */
	Query<T> query();
}
