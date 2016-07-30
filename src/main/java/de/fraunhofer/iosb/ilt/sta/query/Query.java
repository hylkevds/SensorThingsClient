package de.fraunhofer.iosb.ilt.sta.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A query for reading operations.
 *
 * @author Nils Sommer
 *
 */
public class Query<T extends Entity> implements QueryRequest<T>, QueryParameter<T> {

	private final SensorThingsService service;
	private final String pluralizedEntityName;
	private final Class<T> entityClass;
	private final MultivaluedMap<String, String> params = new MultivaluedHashMap<>();

	private final static Logger logger = LoggerFactory.getLogger(Query.class);

	public Query(SensorThingsService service, String pluralizedEntityName, Class<T> entityClass) {
		this.service = service;
		this.pluralizedEntityName = pluralizedEntityName;
		this.entityClass = entityClass;
	}

	public Query<T> filter(String options) {
		this.params.add("$filter", options);

		return this;
	}

	public Query<T> top(int n) {
		this.params.add("$top", Integer.valueOf(n).toString());

		return this;
	}

	public Query<T> orderBy(String clause) {
		this.params.add("$orderby", clause);

		return this;
	}

	public Query<T> skip(int n) {
		this.params.add("$skip", Integer.valueOf(n).toString());

		return this;
	}

	public Query<T> count() {
		this.params.add("$count", "true");

		return this;
	}

	public T first() throws ServiceFailureException {
		return this.list().toList().get(0);
	}

	@SuppressWarnings("unchecked")
	public EntityList<T> list() throws ServiceFailureException {
		EntityList<T> list = new EntityList<>(EntityType.listForClass(entityClass));

		final Client client = this.service.newClient();
		WebTarget target = client
				.target(this.service.getEndpoint())
				.path(this.pluralizedEntityName);

		for (Map.Entry<String, List<String>> paramsByKey : this.params.entrySet()) {
			for (String value : paramsByKey.getValue()) {
				target = target.queryParam(paramsByKey.getKey(), value);
			}
		}

		final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

		if (response.getStatusInfo().getFamily() != Family.SUCCESSFUL) {
			throw new ServiceFailureException(response.getStatusInfo().getReasonPhrase());
		}

		final ObjectMapper mapper = ObjectMapperFactory.<T>getForEntityList(this.entityClass);

		try {
			list = mapper.readValue(response.readEntity(String.class),
					EntityList.class);
		} catch (IOException e) {
			logger.error("Failed deserializing collection.", e);
		}
		list.setService(service, entityClass);
		return list;
	}

	public T last() throws ServiceFailureException {
		final List<T> list = this.list().toList();
		return list.get(list.size() - 1);
	}
}
