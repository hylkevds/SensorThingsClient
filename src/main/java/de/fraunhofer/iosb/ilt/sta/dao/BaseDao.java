package de.fraunhofer.iosb.ilt.sta.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.query.Expansion;
import de.fraunhofer.iosb.ilt.sta.query.Query;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The entity independent implementation of a data access object. Specific
 * entity Daos can be implemented by inheriting from this class and supplying
 * three arguments in the constructor.
 *
 * @author Nils Sommer
 *
 * @param <T> the entity's type
 */
public abstract class BaseDao<T extends Entity> implements Dao<T> {

	private final SensorThingsService service;
	private final String pluralizedEntityName;
	private final Class<T> entityClass;

	private static final Logger logger = LoggerFactory.getLogger(BaseDao.class);

	/**
	 * Constructor.
	 *
	 * @param service the service to operate on
	 * @param pluralizedEntityName pluralized name of the entity type needed to
	 * construct request URIs
	 * @param entityClass the class of the entity's type
	 */
	public BaseDao(SensorThingsService service, String pluralizedEntityName, Class<T> entityClass) {
		this.service = service;
		this.pluralizedEntityName = pluralizedEntityName;
		this.entityClass = entityClass;
	}

	public void create(T entity) throws ServiceFailureException {
		final Client client = this.service.newClient();
		final WebTarget target = client.target(this.service.getEndpoint())
				.path(this.pluralizedEntityName);
		final ObjectMapper mapper = ObjectMapperFactory.get();
		String json;

		try {
			json = mapper.writeValueAsString(entity);
		} catch (JsonProcessingException e) {
			throw new ServiceFailureException(e);
		}

		final Response response = target.request()
				.post(javax.ws.rs.client.Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL) {
			throw new ServiceFailureException(response.getStatusInfo().getReasonPhrase());
		}

		String newLocation = response.getHeaderString("location");
		int pos1 = newLocation.indexOf('(') + 1;
		int pos2 = newLocation.indexOf(')', pos1);
		String stringId = newLocation.substring(pos1, pos2);
		entity.setId(Long.valueOf(stringId));
	}

	public T find(Long id) throws ServiceFailureException {
		final Client client = this.service.newClient();
		final WebTarget target = client.target(this.service.getEndpoint())
				.path(this.entityPath(id));
		final Response response = target.request().accept(MediaType.APPLICATION_JSON_TYPE).get();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL) {
			throw new ServiceFailureException(response.getStatusInfo().getReasonPhrase());
		}

		// TODO: Use Jackson Provider for jersey.
		final ObjectMapper mapper = ObjectMapperFactory.get();

		try {
			return mapper.readValue(response.readEntity(String.class), this.entityClass);
		} catch (IOException e) {
			logger.error("Failed to instantiate entity from JSON response.", e);
			throw new ServiceFailureException(e);
		}
	}

	public T find(URI uri) throws ServiceFailureException {
		final Client client = this.service.newClient();
		final WebTarget target = client.target(uri);
		final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL) {
			throw new ServiceFailureException(response.getStatusInfo().getReasonPhrase());
		}

		final ObjectMapper mapper = ObjectMapperFactory.get();

		try {
			return mapper.readValue(response.readEntity(String.class), this.entityClass);
		} catch (IOException e) {
			logger.error("Failed to instantiate entity from JSON response.", e);
			throw new ServiceFailureException(e);
		}
	}

	public T find(Long id, Expansion expansion) throws ServiceFailureException {
		final Client client = this.service.newClient();
		final WebTarget target = client.target(this.service.getEndpoint())
				.path(this.entityPath(id))
				.queryParam("$expand", expansion.toString());
		final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL) {
			throw new ServiceFailureException(response.getStatusInfo().getReasonPhrase());
		}

		final ObjectMapper mapper = ObjectMapperFactory.get();

		try {
			return mapper.readValue(response.readEntity(String.class), this.entityClass);
		} catch (IOException e) {
			logger.error("Failed to instantiate entity from JSON response.", e);
			throw new ServiceFailureException(e);
		}
	}

	public void update(T entity) throws ServiceFailureException {
		final Client client = this.service.newClient();
		final WebTarget target = client.target(this.service.getEndpoint())
				.path(this.entityPath(entity.getId()));

		final ObjectMapper mapper = ObjectMapperFactory.get();
		String json;

		try {
			json = mapper.writeValueAsString(entity);
		} catch (JsonProcessingException e) {
			throw new ServiceFailureException(e);
		}

		// java's HttpURLConnection doesn't support PATCH, therefore call
		// with string arg "PATCH".
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.method("PATCH",
						javax.ws.rs.client.Entity.entity(json, MediaType.APPLICATION_JSON_TYPE)
				);

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL) {
			throw new ServiceFailureException(response.getStatusInfo().getReasonPhrase());
		}
	}

	public void delete(T entity) {
		final Client client = this.service.newClient();
		final WebTarget target = client.target(this.service.getEndpoint())
				.path(this.entityPath(entity.getId()));
		target.request().delete();
	}

	public Query<T> query() {
		return new Query<T>(this.service, this.pluralizedEntityName, this.entityClass);
	}

	private String entityPath(Long id) {
		return String.format("%s(%d)", this.pluralizedEntityName, id);
	}
}
