package de.fraunhofer.iosb.ilt.sta.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchOperation;
import de.fraunhofer.iosb.ilt.sta.MqttException;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.Utils;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityProperty;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.Id;
import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.IdString;
import de.fraunhofer.iosb.ilt.sta.query.Expansion;
import de.fraunhofer.iosb.ilt.sta.query.Query;
import de.fraunhofer.iosb.ilt.sta.service.MqttSubscription;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The entity independent implementation of a data access object. Specific
 * entity Daos can be implemented by inheriting from this class and supplying
 * three arguments in the constructor.
 *
 * @author Nils Sommer, Hylke van der Schaaf, Michael Jacoby
 *
 * @param <T> the entity's type
 */
public abstract class BaseDao<T extends Entity<T>> implements Dao<T> {

    public static final ContentType APPLICATION_JSON_PATCH = ContentType.create("application/json-patch+json", Consts.UTF_8);
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);
    private final SensorThingsService service;
    private final EntityType plural;
    private final EntityType singular;
    private final Class<T> entityClass;
    private Entity<?> parent;

    /**
     * Constructor.
     *
     * @param service the service to operate on
     * @param entityClass the class of the entity's type
     */
    public BaseDao(SensorThingsService service, Class<T> entityClass) {
        this.service = service;
        this.plural = EntityType.listForClass(entityClass);
        this.singular = EntityType.singleForClass(entityClass);
        this.entityClass = entityClass;
    }

    public BaseDao(SensorThingsService service, Class<T> entityClass, Entity<?> parent) {
        this(service, entityClass);
        this.parent = parent;
    }

    public BaseDao<T> setParent(Entity<?> parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public void create(T entity) throws ServiceFailureException {
        if (parent != null && !parent.getType().hasRelationTo(plural)) {
            throw new IllegalArgumentException("Can not create entity, not a list");
        }

        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(service.getFullPath(parent, plural).toURI());
            final ObjectMapper mapper = ObjectMapperFactory.get();
            String json = mapper.writeValueAsString(entity);

            HttpPost httpPost = new HttpPost(uriBuilder.build());
            LOGGER.debug("Posting to: {}", httpPost.getURI());
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            response = service.execute(httpPost);
            Utils.throwIfNotOk(httpPost, response);

            Header locationHeader = response.getLastHeader("location");
            if (locationHeader == null) {
                throw new IllegalStateException("Server did not send a location header for the new entitiy.");
            }
            String newLocation = locationHeader.getValue();
            int pos1 = newLocation.indexOf('(') + 1;
            int pos2 = newLocation.indexOf(')', pos1);
            String stringId = newLocation.substring(pos1, pos2);
            entity.setId(Id.tryToParse(stringId));
            entity.setService(service);
        } catch (IOException | URISyntaxException exc) {
            throw new ServiceFailureException("Failed to create entity.", exc);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ex) {
            }
        }

    }

    @Override
    public T find(Entity<?> parent) throws ServiceFailureException {
        try {
            URL fullPath = service.getFullPath(parent, singular);
            return find(fullPath.toURI());
        } catch (URISyntaxException ex) {
            throw new ServiceFailureException(ex);
        }
    }

    @Override
    public T find(Id id) throws ServiceFailureException {
        try {
            URIBuilder uriBuilder = new URIBuilder(service.getEndpoint().toString() + this.entityPath(id));
            return find(uriBuilder.build());
        } catch (URISyntaxException ex) {
            throw new ServiceFailureException(ex);
        }
    }

    /**
     * Find the entity with the given Long id. This is a shorthand for find(new
     * IdLong(id));
     *
     * @param id the entity's unique id
     * @return the entity
     * @throws ServiceFailureException the operation failed
     */
    public T find(long id) throws ServiceFailureException {
        return find(new IdLong(id));
    }

    /**
     * Find the entity with the given String id. This is a shorthand for
     * find(new IdLong(id));
     *
     * @param id the entity's unique id
     * @return the entity
     * @throws ServiceFailureException the operation failed
     */
    public T find(String id) throws ServiceFailureException {
        return find(new IdString(id));
    }

    @Override
    public T find(URI uri) throws ServiceFailureException {
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(uri);
            LOGGER.debug("Fetching: {}", uri);
            httpGet.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());

            response = service.execute(httpGet);
            Utils.throwIfNotOk(httpGet, response);

            String returnContent = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            final ObjectMapper mapper = ObjectMapperFactory.get();
            T entity = mapper.readValue(returnContent, entityClass);
            entity.setService(service);
            return entity;
        } catch (IOException | ParseException ex) {
            throw new ServiceFailureException(ex);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public T find(Id id, Expansion expansion) throws ServiceFailureException {
        try {
            URIBuilder uriBuilder = new URIBuilder(service.getEndpoint().toString() + this.entityPath(id));
            uriBuilder.addParameter("$expand", expansion.toString());
            return find(uriBuilder.build());
        } catch (URISyntaxException ex) {
            throw new ServiceFailureException(ex);
        }
    }

    @Override
    public void update(T entity) throws ServiceFailureException {
        HttpPatch httpPatch;
        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(service.getEndpoint().toString() + this.entityPath(entity.getId()));
            final ObjectMapper mapper = ObjectMapperFactory.get();
            String json = mapper.writeValueAsString(entity);

            httpPatch = new HttpPatch(uriBuilder.build());
            LOGGER.debug("Patching: {}", httpPatch.getURI());
            httpPatch.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            response = service.execute(httpPatch);
            Utils.throwIfNotOk(httpPatch, response);

        } catch (JsonProcessingException | URISyntaxException ex) {
            throw new ServiceFailureException(ex);
        } catch (IOException ex) {
            throw new ServiceFailureException(ex);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public void patch(T entity, List<JsonPatchOperation> patch) throws ServiceFailureException {
        HttpPatch httpPatch;
        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(service.getEndpoint().toString() + this.entityPath(entity.getId()));
            final ObjectMapper mapper = ObjectMapperFactory.get();
            String json = mapper.writeValueAsString(patch);

            httpPatch = new HttpPatch(uriBuilder.build());
            LOGGER.debug("Patching: {} with patch {}", httpPatch.getURI(), patch);
            httpPatch.setEntity(new StringEntity(json, APPLICATION_JSON_PATCH));

            response = service.execute(httpPatch);
            Utils.throwIfNotOk(httpPatch, response);

        } catch (JsonProcessingException | URISyntaxException ex) {
            throw new ServiceFailureException(ex);
        } catch (IOException ex) {
            throw new ServiceFailureException(ex);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public void delete(T entity) throws ServiceFailureException {
        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(service.getEndpoint().toString() + this.entityPath(entity.getId()));
            HttpDelete httpDelete = new HttpDelete(uriBuilder.build());
            LOGGER.debug("Deleting: {}", httpDelete.getURI());

            response = service.execute(httpDelete);
            Utils.throwIfNotOk(httpDelete, response);

        } catch (IOException | URISyntaxException ex) {
            throw new ServiceFailureException(ex);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public Query<T> query() {
        return new Query<>(this.service, this.entityClass, this.parent);
    }

    private String entityPath(Id id) {
        return String.format("%s(%s)", this.plural.getName(), id.getUrl());
    }

    protected SensorThingsService getService() {
        return service;
    }

    @Override
    public MqttSubscription subscribe(Consumer<T> handler) throws MqttException {
        return service.subscribe(getMqttTopic(), handler, entityClass, null);
    }

    @Override
    public MqttSubscription subscribe(Predicate<T> filter, Consumer<T> handler) throws MqttException {
        return service.subscribe(getMqttTopic(), handler, entityClass, filter);
    }

    private void validatePropertySelect(EntityProperty... properties) throws MqttException {
        if (Stream.of(properties).anyMatch(x -> !singular.hasProperty(x))) {
            throw new MqttException("use of unknown property in $select. Allowed properties for type " + singular.getName() + ": "
                    + singular.getProperties().stream().
                            map(x -> x.getName())
                            .collect(Collectors.joining(", ")));
        }
    }

    @Override
    public MqttSubscription subscribe(Predicate<T> filter, Consumer<T> handler, EntityProperty... properties) throws MqttException {
        validatePropertySelect(properties);
        return service.subscribe(getMqttTopic(Arrays.asList(properties)), handler, entityClass, filter);
    }

    @Override
    public MqttSubscription subscribe(Consumer<T> handler, EntityProperty... properties) throws MqttException {
        validatePropertySelect(properties);
        return service.subscribe(getMqttTopic(Arrays.asList(properties)), handler, entityClass, null);
    }

    public MqttSubscription subscribe(Entity entity, Consumer<T> handler) throws MqttException {
        return service.subscribe(getMqttTopic(entity), handler, entityClass, null);
    }

    protected String getMqttTopic() {
        if (parent != null && !parent.getType().hasRelationTo(plural)) {
            throw new IllegalArgumentException("Cannot create entity, not a list");
        }
        return service.getVersion().getUrlPattern() + "/" + service.getPath(parent, plural);
    }

    protected String getMqttTopic(Entity entity) throws MqttException {
        if (parent != null && !parent.getType().hasRelationTo(plural)) {
            throw new IllegalArgumentException("Cannot create entity, not a list");
        }
        return service.getVersion().getUrlPattern() + "/" + entityPath(entity.getId());
    }

    protected String getMqttTopic(List<EntityProperty> properties) throws MqttException {
        if (parent != null && !parent.getType().hasRelationTo(plural)) {
            throw new IllegalArgumentException("Cannot create entity, not a list");
        }
        String result = service.getVersion().getUrlPattern() + "/" + service.getPath(parent, plural);
        if (properties != null && !properties.isEmpty()) {
            if (properties.stream().anyMatch(x -> !singular.hasProperty(x))) {
                throw new MqttException("Property not defined for this entity. Allowed properties: " + singular.getProperties());
            }
            result += "?$select=" + properties.stream().map(x -> x.getName()).collect(Collectors.joining(","));
        }
        return result;
    }

    @Override
    public void unsubscribe(MqttSubscription subscription) throws MqttException {
        service.unsubscribe(subscription);
    }

    @Override
    public void unsubscribe() throws MqttException {
        service.unsubscribe(getMqttTopic());
    }
}
