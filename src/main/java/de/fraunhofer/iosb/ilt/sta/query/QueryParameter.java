package de.fraunhofer.iosb.ilt.sta.query;

import de.fraunhofer.iosb.ilt.sta.model.Entity;

/**
 * Request parameters a query should support.
 *
 * @author Nils Sommer
 *
 * @param <T> the query's entity type
 */
public interface QueryParameter<T extends Entity<T>> {

	/**
	 * Add the filter parameter as specified by the SensorThingsAPI
	 * specification.
	 *
	 * @param options the filter options as a string
	 * @return the updated instance of the query
	 */
	public Query<T> filter(String options);

	/**
	 * Add the top parameter as specified by the SensorThingsAPI specification.
	 *
	 * @param n the limit
	 * @return the updated instance of the query
	 */
	public Query<T> top(int n);

	/**
	 * Add the orderBy parameter as specified by the SensorThingsAPI
	 * specification.
	 *
	 * @param clause the order clause
	 * @return the updated instance of the query
	 */
	public Query<T> orderBy(String clause);

	/**
	 * Add the skip parameter as specified by the SensorThingsAPI specification.
	 *
	 * @param n the number of entities to skip
	 * @return the updated instance of the query
	 */
	public Query<T> skip(int n);

	/**
	 * Add the count parameter as specified by the SensorThingsAPI
	 * specification.
	 *
	 * @return the updated instance of the query
	 */
	public Query<T> count();
}
