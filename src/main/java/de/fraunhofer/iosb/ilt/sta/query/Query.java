package de.fraunhofer.iosb.ilt.sta.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A query for reading operations.
 *
 * @author Nils Sommer, Hylke van der Schaaf
 * @param <T> The type of entity this query returns.
 */
public class Query<T extends Entity<T>> implements QueryRequest<T>, QueryParameter<T> {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Query.class);
	private final SensorThingsService service;
	private final EntityType plural;
	private final Class<T> entityClass;
	private final Entity<?> parent;
	private final List<NameValuePair> params = new ArrayList<>();

	public Query(SensorThingsService service, Class<T> entityClass) {
		this(service, entityClass, null);
	}

	public Query(SensorThingsService service, Class<T> entityClass, Entity<?> parent) {
		this.service = service;
		this.plural = EntityType.listForClass(entityClass);
		this.entityClass = entityClass;
		this.parent = parent;
	}

	public EntityType getEntityType() {
		return plural;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public SensorThingsService getService() {
		return service;
	}

	@Override
	public Query<T> filter(String options) {
		this.params.add(new BasicNameValuePair("$filter", options));

		return this;
	}

	@Override
	public Query<T> top(int n) {
		this.params.add(new BasicNameValuePair("$top", Integer.toString(n)));

		return this;
	}

	@Override
	public Query<T> orderBy(String clause) {
		this.params.add(new BasicNameValuePair("$orderby", clause));

		return this;
	}

	@Override
	public Query<T> skip(int n) {
		this.params.add(new BasicNameValuePair("$skip", Integer.toString(n)));

		return this;
	}

	@Override
	public Query<T> count() {
		params.add(new BasicNameValuePair("$count", "true"));
		return this;
	}

	public Query<T> expand(Expansion expansion) {
		params.add(new BasicNameValuePair("$expand", expansion.toString()));
		return this;
	}

	public Query<T> expand(String expansion) {
		params.add(new BasicNameValuePair("$expand", expansion));
		return this;
	}

	public Query<T> select(String... fields) {
		StringBuilder selectValue = new StringBuilder();
		for (String field : fields) {
			selectValue.append(field).append(",");
		}
		params.add(new BasicNameValuePair("$select", selectValue.substring(0, selectValue.length() - 1)));
		return this;
	}

	@Override
	public T first() throws ServiceFailureException {
		this.top(1);
		List<T> asList = this.list().toList();
		if (asList.isEmpty()) {
			return null;
		}
		return asList.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EntityList<T> list() throws ServiceFailureException {
		EntityList<T> list = new EntityList<>(plural);

		URIBuilder uriBuilder = new URIBuilder(service.getFullPath(parent, plural));
		uriBuilder.addParameters(params);
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(uriBuilder.build());
			LOGGER.debug("Fetching: {}", httpGet.getURI());
			httpGet.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());

			response = service.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			if (code < 200 || code >= 300) {
				throw new IllegalArgumentException(EntityUtils.toString(response.getEntity(), Consts.UTF_8));
			}
			String json = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			final ObjectMapper mapper = ObjectMapperFactory.get();
			list = mapper.readValue(json, plural.getTypeReference());
		} catch (URISyntaxException | IOException ex) {
			LOGGER.error("Failed to fetch list.", ex);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException ex) {
			}
		}

		list.setService(service, entityClass);
		return list;
	}

}
