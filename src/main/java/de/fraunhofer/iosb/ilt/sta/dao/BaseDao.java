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
import java.net.URISyntaxException;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
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
		final CloseableHttpClient client = this.service.getClient();
		URIBuilder uriBuilder = new URIBuilder(this.service.getEndpoint().resolve(pluralizedEntityName));

		try {

			final ObjectMapper mapper = ObjectMapperFactory.get();
			String json = mapper.writeValueAsString(entity);

			HttpPost httpPost = new HttpPost(uriBuilder.build());
			httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

			CloseableHttpResponse response = client.execute(httpPost);

			if (response.getStatusLine().getStatusCode() != 201) {
				throw new ServiceFailureException(response.getStatusLine().getReasonPhrase());
			}

			Header locationHeader = response.getLastHeader("location");
			if (locationHeader == null) {
				throw new IllegalStateException("Server did not send a location header for the new entitiy.");
			}
			String newLocation = locationHeader.getValue();
			int pos1 = newLocation.indexOf('(') + 1;
			int pos2 = newLocation.indexOf(')', pos1);
			String stringId = newLocation.substring(pos1, pos2);
			entity.setId(Long.valueOf(stringId));
		} catch (JsonProcessingException | URISyntaxException e) {
			throw new ServiceFailureException(e);
		} catch (IOException e) {
			throw new ServiceFailureException(e);
		}

	}

	@Override
	public T find(Long id) throws ServiceFailureException {
		URIBuilder uriBuilder = new URIBuilder(this.service.getEndpoint().resolve(this.entityPath(id)));
		try {
			return find(uriBuilder.build());
		} catch (URISyntaxException ex) {
			throw new ServiceFailureException(ex);
		}
	}

	@Override
	public T find(URI uri) throws ServiceFailureException {
		CloseableHttpClient client = this.service.getClient();
		try {
			HttpGet httpGet = new HttpGet(uri);
			httpGet.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
			CloseableHttpResponse response = client.execute(httpGet);
			String json = EntityUtils.toString(response.getEntity(), Consts.UTF_8);

			final ObjectMapper mapper = ObjectMapperFactory.get();
			return mapper.readValue(json, this.entityClass);

		} catch (IOException | ParseException ex) {
			throw new ServiceFailureException(ex);
		}
	}

	@Override
	public T find(Long id, Expansion expansion) throws ServiceFailureException {
		URIBuilder uriBuilder = new URIBuilder(this.service.getEndpoint().resolve(this.entityPath(id)));
		uriBuilder.addParameter("$expand", expansion.toString());
		try {
			return find(uriBuilder.build());
		} catch (URISyntaxException ex) {
			throw new ServiceFailureException(ex);
		}
	}

	@Override
	public void update(T entity) throws ServiceFailureException {
		final CloseableHttpClient client = this.service.getClient();
		URIBuilder uriBuilder = new URIBuilder(this.service.getEndpoint().resolve(this.entityPath(entity.getId())));

		try {

			final ObjectMapper mapper = ObjectMapperFactory.get();
			String json = mapper.writeValueAsString(entity);

			HttpPatch httpPatch = new HttpPatch(uriBuilder.build());
			httpPatch.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

			CloseableHttpResponse response = client.execute(httpPatch);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new ServiceFailureException(response.getStatusLine().getReasonPhrase());
			}

		} catch (JsonProcessingException | URISyntaxException ex) {
			throw new ServiceFailureException(ex);
		} catch (IOException ex) {
			throw new ServiceFailureException(ex);
		}

	}

	@Override
	public void delete(T entity) throws ServiceFailureException {
		final CloseableHttpClient client = this.service.getClient();
		URIBuilder uriBuilder = new URIBuilder(this.service.getEndpoint().resolve(this.entityPath(entity.getId())));
		try {
			HttpDelete delete = new HttpDelete(uriBuilder.build());
			client.execute(delete);
		} catch (IOException | URISyntaxException ex) {
			throw new ServiceFailureException(ex);
		}
	}

	@Override
	public Query<T> query() {
		return new Query<T>(this.service, this.pluralizedEntityName, this.entityClass);
	}

	private String entityPath(Long id) {
		return String.format("%s(%d)", this.pluralizedEntityName, id);
	}
}
