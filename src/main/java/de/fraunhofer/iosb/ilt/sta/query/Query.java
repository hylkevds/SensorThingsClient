package de.fraunhofer.iosb.ilt.sta.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.Utils;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
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

    private void removeAllParams(String key) {
        for (Iterator<NameValuePair> it = params.iterator(); it.hasNext();) {
            NameValuePair param = it.next();
            if (param.getName().equals(key)) {
                it.remove();
                break;
            }
        }
    }

    @Override
    public Query<T> filter(String options) {
        removeAllParams("$filter");
        if (options.isEmpty()) {
            return this;
        }
        params.add(new BasicNameValuePair("$filter", options));
        return this;
    }

    @Override
    public Query<T> top(int n) {
        removeAllParams("$top");
        params.add(new BasicNameValuePair("$top", Integer.toString(n)));
        return this;
    }

    @Override
    public Query<T> orderBy(String clause) {
        removeAllParams("$orderby");
        params.add(new BasicNameValuePair("$orderby", clause));
        return this;
    }

    @Override
    public Query<T> skip(int n) {
        removeAllParams("$skip");
        params.add(new BasicNameValuePair("$skip", Integer.toString(n)));
        return this;
    }

    @Override
    public Query<T> count() {
        removeAllParams("$count");
        params.add(new BasicNameValuePair("$count", "true"));
        return this;
    }

    public Query<T> expand(Expansion expansion) {
        removeAllParams("$expand");
        params.add(new BasicNameValuePair("$expand", expansion.toString()));
        return this;
    }

    public Query<T> expand(String expansion) {
        removeAllParams("$expand");
        params.add(new BasicNameValuePair("$expand", expansion));
        return this;
    }

    public Query<T> select(String... fields) {
        removeAllParams("$select");
        if (fields == null) {
            return this;
        }
        StringBuilder selectValue = new StringBuilder();
        for (String field : fields) {
            selectValue.append(field).append(",");
        }
        if (selectValue.length() == 0) {
            return this;
        }
        String select = selectValue.substring(0, selectValue.length() - 1);
        if (select.isEmpty()) {
            return this;
        }
        params.add(new BasicNameValuePair("$select", select));
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

        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(service.getFullPath(parent, plural).toURI());
            uriBuilder.addParameters(params);
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            LOGGER.debug("Fetching: {}", httpGet.getURI());
            httpGet.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());

            response = service.execute(httpGet);
            Utils.throwIfNotOk(response);

            String json = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            final ObjectMapper mapper = ObjectMapperFactory.get();
            list = mapper.readValue(json, plural.getTypeReference());
        } catch (URISyntaxException | IOException exc) {
            throw new ServiceFailureException("Failed to fetch entities from query.", exc);
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

    public void delete() throws ServiceFailureException {
        removeAllParams("$top");
        removeAllParams("$skip");
        removeAllParams("$count");
        removeAllParams("$select");
        removeAllParams("$expand");

        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(service.getFullPath(parent, plural).toURI());
            uriBuilder.addParameters(params);
            HttpDelete httpDelete = new HttpDelete(uriBuilder.build());
            LOGGER.debug("Deleting: {}", httpDelete.getURI());
            httpDelete.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());

            response = service.execute(httpDelete);
            Utils.throwIfNotOk(response);

        } catch (URISyntaxException | IOException exc) {
            throw new ServiceFailureException("Failed to delete from query.", exc);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException exc) {
                LOGGER.error("Exception closing response.", exc);
            }
        }

    }
}
