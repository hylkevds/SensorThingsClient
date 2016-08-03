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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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
	private final List<NameValuePair> params = new ArrayList<>();

	private final static Logger logger = LoggerFactory.getLogger(Query.class);

	public Query(SensorThingsService service, String pluralizedEntityName, Class<T> entityClass) {
		this.service = service;
		this.pluralizedEntityName = pluralizedEntityName;
		this.entityClass = entityClass;
	}

	public Query<T> filter(String options) {
		this.params.add(new BasicNameValuePair("$filter", options));

		return this;
	}

	public Query<T> top(int n) {
		this.params.add(new BasicNameValuePair("$top", Integer.toString(n)));

		return this;
	}

	public Query<T> orderBy(String clause) {
		this.params.add(new BasicNameValuePair("$orderby", clause));

		return this;
	}

	public Query<T> skip(int n) {
		this.params.add(new BasicNameValuePair("$skip", Integer.valueOf(n).toString()));

		return this;
	}

	public Query<T> count() {
		this.params.add(new BasicNameValuePair("$count", "true"));

		return this;
	}

	public T first() throws ServiceFailureException {
		return this.list().toList().get(0);
	}

	@SuppressWarnings("unchecked")
	public EntityList<T> list() throws ServiceFailureException {
		EntityList<T> list = new EntityList<>(EntityType.listForClass(entityClass));

		URIBuilder uriBuilder = new URIBuilder(this.service.getEndpoint().resolve(this.pluralizedEntityName));
		uriBuilder.addParameters(params);
		final CloseableHttpClient client = service.getClient();
		HttpGet httpGet;
		try {
			httpGet = new HttpGet(uriBuilder.build());
			httpGet.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());

			CloseableHttpResponse response = client.execute(httpGet);
			String json = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			final ObjectMapper mapper = ObjectMapperFactory.<T>getForEntityList(entityClass);
			list = mapper.readValue(json, EntityList.class);
		} catch (URISyntaxException | IOException ex) {
			logger.error("Failed to fetch list.", ex);
		}

		list.setService(service, entityClass);
		return list;
	}

	public T last() throws ServiceFailureException {
		final List<T> list = this.list().toList();
		return list.get(list.size() - 1);
	}
}
