package de.fraunhofer.iosb.ilt.sta.query;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;

/**
 * Request methods a query should support.
 *
 * @author Nils Sommer
 *
 * @param <T> the query's entity type
 */
public interface QueryRequest<T extends Entity<T>> {

	/**
	 * Get the first instance of an entity collection.
	 *
	 * @return the first instance
	 * @throws ServiceFailureException the request failed
	 */
	T first() throws ServiceFailureException;

	/**
	 * Get an entity collection.
	 *
	 * @return the entity collection
	 * @throws ServiceFailureException the request failed
	 */
	EntityList<T> list() throws ServiceFailureException;
}
