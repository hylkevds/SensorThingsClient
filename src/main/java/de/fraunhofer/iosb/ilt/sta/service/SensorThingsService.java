package de.fraunhofer.iosb.ilt.sta.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchOperation;
import de.fraunhofer.iosb.ilt.sta.MqttException;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.StatusCodeException;
import de.fraunhofer.iosb.ilt.sta.Utils;
import de.fraunhofer.iosb.ilt.sta.dao.ActuatorDao;
import de.fraunhofer.iosb.ilt.sta.dao.DatastreamDao;
import de.fraunhofer.iosb.ilt.sta.dao.FeatureOfInterestDao;
import de.fraunhofer.iosb.ilt.sta.dao.HistoricalLocationDao;
import de.fraunhofer.iosb.ilt.sta.dao.LocationDao;
import de.fraunhofer.iosb.ilt.sta.dao.MultiDatastreamDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservationDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservedPropertyDao;
import de.fraunhofer.iosb.ilt.sta.dao.SensorDao;
import de.fraunhofer.iosb.ilt.sta.dao.TaskDao;
import de.fraunhofer.iosb.ilt.sta.dao.TaskingCapabilityDao;
import de.fraunhofer.iosb.ilt.sta.dao.ThingDao;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.ext.DataArrayDocument;
import de.fraunhofer.iosb.ilt.sta.service.ServerSettings.Extension;
import static de.fraunhofer.iosb.ilt.sta.service.ServerSettings.TAG_MQTT_ENDPOINTS;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.LoggerFactory;

/**
 * A SensorThingsService represents the service endpoint of a server.
 *
 * @author Nils Sommer, Hylke van der Schaaf, Michael Jacoby
 */
public class SensorThingsService implements MqttCallback {

    /**
     * The logger for this class.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SensorThingsService.class);

    private URL endpoint;
    private CloseableHttpClient httpClient;
    private TokenManager tokenManager;
    private MqttClient mqttClient;
    private MqttConfig mqttConfig;
    private boolean mqttAutoConfigChecked = false;
    private Map<String, Set<Consumer<MqttMessage>>> mqttSubscriptions = new HashMap<>();
    private SensorThingsAPIVersion version;
    /**
     * The request timeout in MS.
     */
    private int requestTimeoutMs = 120000;

    /**
     * Creates a new SensorThingsService without an endpoint url set. The
     * endpoint url MUST be set before the service can be used.
     */
    public SensorThingsService() {
        this.httpClient = HttpClients.createSystem();
    }

    /**
     * Constructor.
     *
     * @param endpoint the base URI of the SensorThings service
     * @throws java.net.MalformedURLException when building the final url fails.
     */
    public SensorThingsService(URI endpoint) throws MalformedURLException {
        this(endpoint.toURL());
    }

    /**
     * Constructor.
     *
     * @param endpoint the base URL of the SensorThings service
     * @throws java.net.MalformedURLException when building the final url fails.
     */
    public SensorThingsService(URL endpoint) throws MalformedURLException {
        setEndpoint(endpoint);
        this.httpClient = HttpClients.createSystem();
    }

    /**
     * Constructor.
     *
     * @param endpoint the base URL of the SensorThings service
     * @param mqttConfig the config object for MQTT connections
     * @throws java.net.MalformedURLException when building the final url fails.
     */
    public SensorThingsService(URL endpoint, MqttConfig mqttConfig) throws MalformedURLException, MqttException {
        this(endpoint);
        this.mqttConfig = mqttConfig;
        try {
            this.mqttClient = new MqttClient(mqttConfig.getServerUri(), mqttConfig.getClientId(), mqttConfig.getPersistence());
        } catch (org.eclipse.paho.client.mqttv3.MqttException exc) {
            throw new MqttException("could not create MQTT client", exc);
        }
        this.mqttClient.setCallback(this);
    }

    /**
     * Sets the endpoint URL/URI. Once the endpoint URL/URI is set it can not be
     * changed. The endpoint url MUST be set before the service can be used.
     *
     * @param endpoint The URI of the endpoint.
     * @throws java.net.MalformedURLException when building the final url fails.
     */
    public final void setEndpoint(URI endpoint) throws MalformedURLException {
        setEndpoint(endpoint.toURL());
    }

    /**
     * Sets the endpoint URL/URI. Once the endpoint URL/URI is set it can not be
     * changed. The endpoint url MUST be set before the service can be used.
     *
     * @param endpoint The URL of the endpoint.
     * @throws java.net.MalformedURLException when building the final url fails.
     */
    public final void setEndpoint(URL endpoint) throws MalformedURLException {
        if (this.endpoint != null) {
            throw new IllegalStateException("endpoint URL already set.");
        }
        String url = StringUtils.removeEnd(endpoint.toString(), "/");
        String lastSegment = endpoint.getPath().substring(endpoint.getPath().lastIndexOf('/') + 1);
        SensorThingsAPIVersion detectedVersion = SensorThingsAPIVersion.fromString(lastSegment);
        if (detectedVersion != null) {
            version = detectedVersion;
        } else {
            if (getVersion() == null) {
                throw new MalformedURLException("endpoint URL does not contain version (e.g. http://example.org/v1.0/) nor version information explicitely provided");
            }
            url += "/" + getVersion().getUrlPattern();
        }
        cleanupMqtt();
        mqttAutoConfigChecked = false;
        mqttConfig = null;
        this.endpoint = new URL(url + "/");
    }

    /**
     * Gets the endpoint URL for the service. Throws an IllegalStateException if
     * the endpoint is not set.
     *
     * @return the endpoint URL for the service.
     */
    public URL getEndpoint() {
        if (this.endpoint == null) {
            throw new IllegalStateException("endpoint URL not set.");
        }
        return this.endpoint;
    }

    /**
     * Check if the endpoint is set.
     *
     * @return true if the endpoint is set, false otherwise.
     */
    public boolean isEndpointSet() {
        return endpoint != null;
    }

    /**
     * The local path to the entity or collection. e.g.:
     * <ul>
     * <li>Things</li>
     * <li>Things(2)/Datastreams</li>
     * <li>Datastreams(5)/Thing</li>
     * </ul>
     *
     * @param parent The entity holding the relation, can be null.
     * @param relation The relation or collection to get.
     * @return The path to the entity collection.
     */
    public String getPath(Entity<?> parent, EntityType relation) {
        if (parent == null) {
            return relation.getName();
        }
        if (!parent.getType().hasRelationTo(relation)) {
            throw new IllegalArgumentException("Entity of type " + parent.getType() + " has no relation of type " + relation + ".");
        }
        if (parent.getId() == null) {
            throw new IllegalArgumentException("Can not create a path with a parent without id.");
        }
        return String.format("%s(%s)/%s", EntityType.listForClass(parent.getClass()).getName(), parent.getId().getUrl(), relation.getName());
    }

    /**
     * The path to the entity or collection. used for Mqtt e.g.:
     * <ul>
     * <li>Things</li>
     * <li>Things(2)/Datastreams</li>
     * <li>Datastreams(5)/Thing</li>
     * </ul>
     *
     * @param parent The entity holding the relation, can be null.
     * @param relation The relation or collection to get.
     * @return The path to the entity collection.
     */
    public String getMqttPath(Entity<?> parent, EntityType relation) {
        if (parent == null) {
            return relation.getName();
        }
        if (!parent.getType().hasRelationTo(relation)) {
            throw new IllegalArgumentException("Entity of type " + parent.getType() + " has no relation of type " + relation + ".");
        }
        if (parent.getId() == null) {
            throw new IllegalArgumentException("Can not create a path with a parent without id.");
        }
        return String.format("%s(%s)/%s", EntityType.listForClass(parent.getClass()).getName(), parent.getId().getUrl(), relation.getName());
    }

    /**
     * The full path to the entity or collection.
     *
     * @param parent The entity holding the relation, can be null.
     * @param relation The relation or collection to get.
     * @return the full path to the entity or collection.
     * @throws de.fraunhofer.iosb.ilt.sta.ServiceFailureException If generating
     * the path fails.
     */
    public URL getFullPath(Entity<?> parent, EntityType relation) throws ServiceFailureException {
        try {
            return new URL(getEndpoint().toString() + getPath(parent, relation));
        } catch (MalformedURLException exc) {
            LOGGER.error("Failed to generate URL.", exc);
            throw new ServiceFailureException(exc);
        }
    }

    /**
     * Execute the given request, adding a token header if needed.
     *
     * @param request The request to execute.
     * @return the response.
     * @throws IOException in case of problems.
     */
    public CloseableHttpResponse execute(HttpRequestBase request) throws IOException {
        setTimeouts(request);
        if (tokenManager != null) {
            tokenManager.addAuthHeader(request);
        }
        return httpClient.execute(request);
    }

    private void setTimeouts(HttpRequestBase request) {
        RequestConfig.Builder configBuilder;
        if (request.getConfig() == null) {
            configBuilder = RequestConfig.copy(RequestConfig.DEFAULT);
        } else {
            configBuilder = RequestConfig.copy(request.getConfig());
        }
        RequestConfig config = configBuilder
                .setSocketTimeout(requestTimeoutMs)
                .setConnectTimeout(requestTimeoutMs)
                .setConnectionRequestTimeout(requestTimeoutMs)
                .build();
        request.setConfig(config);
    }

    /**
     * @return a new Datastream Dao.
     */
    public DatastreamDao datastreams() {
        return new DatastreamDao(this);
    }

    /**
     * @return a new MultiDatastream Dao.
     */
    public MultiDatastreamDao multiDatastreams() {
        return new MultiDatastreamDao(this);
    }

    /**
     * @return a new FeatureOfInterest Dao.
     */
    public FeatureOfInterestDao featuresOfInterest() {
        return new FeatureOfInterestDao(this);
    }

    /**
     * @return a new HistoricalLocation Dao.
     */
    public HistoricalLocationDao historicalLocations() {
        return new HistoricalLocationDao(this);
    }

    /**
     * @return a new Location Dao.
     */
    public LocationDao locations() {
        return new LocationDao(this);
    }

    /**
     * @return a new Observation Dao.
     */
    public ObservationDao observations() {
        return new ObservationDao(this);
    }

    /**
     * @return a new PbservedProperty Dao.
     */
    public ObservedPropertyDao observedProperties() {
        return new ObservedPropertyDao(this);
    }

    /**
     * @return a new Sensor Dao.
     */
    public SensorDao sensors() {
        return new SensorDao(this);
    }

    /**
     * @return a new Thing Dao.
     */
    public ThingDao things() {
        return new ThingDao(this);
    }

    /**
     * @return a new Actuator Dao.
     */
    public ActuatorDao actuators() {
        return new ActuatorDao(this);
    }

    /**
     * @return a new Task Dao.
     */
    public TaskDao tasks() {
        return new TaskDao(this);
    }

    /**
     * @return a new TaskingCapability Dao.
     */
    public TaskingCapabilityDao taskingCapabilities() {
        return new TaskingCapabilityDao(this);
    }

    /**
     *
     * @param dataArray The Observations to create.
     * @return The response of the service.
     * @throws ServiceFailureException in case the server rejects the POST.
     */
    public List<String> create(DataArrayDocument dataArray) throws ServiceFailureException {
        return new ObservationDao(this).create(dataArray);
    }

    /**
     * Create the given entity in this service. Executes a POST to the
     * Collection of the entity type. The entity will be updated with the ID of
     * the entity in the Service and it will be linked to the Service.
     *
     * @param <T> The type of entity to create. Inferred from the entity.
     * @param entity The entity to create in the service.
     * @throws ServiceFailureException in case the server rejects the POST.
     */
    public <T extends Entity<T>> void create(T entity) throws ServiceFailureException {
        entity.getDao(this).create(entity);
    }

    /**
     * Patches the entity in the Service.
     *
     * @param <T> The type of entity to update. Inferred from the entity.
     * @param entity The entity to update in the service.
     * @throws ServiceFailureException in case the server rejects the PATCH.
     */
    public <T extends Entity<T>> void update(T entity) throws ServiceFailureException {
        entity.getDao(this).update(entity);
    }

    /**
     * Update the given entity with the given patch. Does not update the entity
     * object itself. To see the result, fetch it anew from the server.
     *
     * @param <T> The type of entity to update. Inferred from the entity.
     * @param entity The entity to update on the server.
     * @param patch The patch to apply to the entity.
     * @throws ServiceFailureException in case the server rejects the PATCH.
     */
    public <T extends Entity<T>> void patch(T entity, List<JsonPatchOperation> patch) throws ServiceFailureException {
        entity.getDao(this).patch(entity, patch);
    }

    /**
     * Deletes the given entity from the service.
     *
     * @param <T> The type of entity to delete. Inferred from the entity.
     * @param entity The entity to delete in the service.
     * @throws ServiceFailureException in case the server rejects the DELETE.
     */
    public <T extends Entity<T>> void delete(T entity) throws ServiceFailureException {
        entity.getDao(this).delete(entity);
    }

    /**
     * Sets the TokenManager. Before each request is sent to the Service, the
     * TokenManager has the opportunity to modify the request and add any
     * headers required for Authentication and Authorisation.
     *
     * @param tokenManager The TokenManager to use, can be null.
     * @return This SensorThingsService.
     */
    public SensorThingsService setTokenManager(TokenManager tokenManager) {
        if (tokenManager != null && tokenManager.getHttpClient() == null) {
            tokenManager.setHttpClient(httpClient);
        }
        this.tokenManager = tokenManager;
        return this;
    }

    /**
     * @return The current TokenManager.
     */
    public TokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Get the httpclient used for requests.
     *
     * @return the client
     */
    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Set the httpclient used for requests.
     *
     * @param httpClient the client to set
     */
    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void connectionLost(Throwable e) {
        LOGGER.warn("MQTT connection lost", e);
    }

    /**
     * Start a MQTT subscription
     *
     * @param <T> return type
     * @param topic The MQTT topic to subscribe
     * @param handler is called when a new notification happens (if result
     * satisfies the filter)
     * @param returnType type to cast the result to
     * @param filter if not null, filters incoming notifications before handler
     * is called
     * @return the client
     * @throws MqttException when subscription fails
     */
    public <T> MqttSubscription subscribe(String topic, Consumer<T> handler, Class<T> returnType, Predicate<T> filter) throws MqttException {
        Consumer<MqttMessage> typedHandler = x -> {
            try {
                T entity = ObjectMapperFactory.get().readValue(x.getPayload(), returnType);
                if (filter == null || filter.test(entity)) {
                    handler.accept(entity);
                }
            } catch (Exception exc) {
                LOGGER.warn("could not parse payload received via MQTT");
            }
        };
        subscribe(topic, typedHandler);
        return new MqttSubscription(topic, typedHandler);
    }

    /**
     * Start a MQTT subscription
     *
     * @param topic The MQTT topic to subscribe
     * @param handler is called when a new notification happens (if result
     * satisfies the filter)
     * @return the client
     * @throws MqttException when subscription fails
     */
    public MqttSubscription subscribe(String topic, Consumer<MqttMessage> handler) throws MqttException {
        checkMqttConfigured();
        if (!mqttSubscriptions.containsKey(topic)) {
            mqttSubscriptions.put(topic, new HashSet<>());
        }
        if (mqttSubscriptions.get(topic).add(handler) && mqttSubscriptions.get(topic).size() == 1) {
            checkMqttConnected();
            try {
                mqttClient.subscribe(topic);
            } catch (org.eclipse.paho.client.mqttv3.MqttException exc) {
                throw new MqttException(String.format("subscribing topic '%s' failed", topic), exc);
            }
        }
        return new MqttSubscription(topic, handler);
    }

    /**
     * Publish entity via MQTT on given topic
     *
     * @param topic The MQTT topic to publish to
     * @param entity entity to publish
     * @throws MqttException when publication fails
     */
    public void publish(String topic, Entity entity) throws MqttException {
        checkMqttConfigured();
        checkMqttConnected();
        final ObjectMapper mapper = ObjectMapperFactory.get();
        try {
            mqttClient.publish(topic, new MqttMessage(ObjectMapperFactory.get().writeValueAsBytes(entity)));
        } catch (JsonProcessingException ex) {
            throw new MqttException("Could not process JSON", ex);
        } catch (org.eclipse.paho.client.mqttv3.MqttException ex) {
            throw new MqttException("Error publishing via MQTT", ex);
        }
    }

    private void checkMqttConnected() throws MqttException {
        if (mqttClient.isConnected()) {
            return;
        }
        try {
            mqttClient.connect(mqttConfig.getOptions());
        } catch (org.eclipse.paho.client.mqttv3.MqttException exc) {
            throw new MqttException("MQTT connection failed", exc);
        }
    }

    private void checkMqttConfigured() throws MqttException {
        if (mqttClient == null) {
            if (mqttConfig == null) {
                LOGGER.info("trying to auto-configure MQTT connection");
                mqttConfig = getRemoteConfig();
            }
            if (mqttConfig == null) {
                throw new MqttException("you must configure MQTT to use this feature");
            }
            try {
                mqttClient = new MqttClient(mqttConfig.getServerUri(), mqttConfig.getClientId(), mqttConfig.getPersistence());
            } catch (org.eclipse.paho.client.mqttv3.MqttException exc) {
                throw new MqttException("could not create MQTT client", exc);
            }
        }
    }

    private MqttConfig getRemoteConfig() {
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(getEndpoint().toURI());
            LOGGER.debug("Fetching: {}", getEndpoint().toURI());
            httpGet.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());

            response = execute(httpGet);
            Utils.throwIfNotOk(httpGet, response);

            String returnContent = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            final ObjectMapper mapper = ObjectMapperFactory.get();
            JsonNode root = mapper.readTree(returnContent);
            if (root.has(ServerSettings.TAG_SERVER_SETTINGS)) {
                ServerSettings serverSettings = mapper.treeToValue(root.get(ServerSettings.TAG_SERVER_SETTINGS), ServerSettings.class);
                return new MqttConfig(((List<String>) serverSettings.getExtensions().get(Extension.MQTT).get(TAG_MQTT_ENDPOINTS)).get(0));
            }
        } catch (IOException | ParseException ex) {
            LOGGER.warn("");
        } catch (StatusCodeException ex) {
            Logger.getLogger(SensorThingsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(SensorThingsService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ex) {
            }
        }
        return null;
    }

    public void unsubscribe(MqttSubscription subscription) throws MqttException {
        unsubscribe(subscription.getTopic(), subscription.getHandler());
    }

    public void unsubscribe(String topic, Consumer<MqttMessage> handler) throws MqttException {
        checkMqttConfigured();
        if (mqttSubscriptions.containsKey(topic)) {
            if (mqttSubscriptions.get(topic).size() == 1 && mqttSubscriptions.get(topic).contains(handler)) {
                unsubscribeMqtt(topic);
            }
            mqttSubscriptions.get(topic).remove(handler);
            cleanupMqttSubscriptions();
        }
    }

    private void cleanupMqttSubscriptions() throws MqttException {
        mqttSubscriptions.entrySet().removeIf(x -> x.getValue().isEmpty());
    }

    private void unsubscribeMqtt(String topic) throws MqttException {
        try {
            mqttClient.unsubscribe(topic);

        } catch (org.eclipse.paho.client.mqttv3.MqttException exc) {
            throw new MqttException(String.format("could not unsubscribe from MQTT '%s'", topic), exc);
        }
        try {
            if (mqttSubscriptions.isEmpty()) {
                mqttClient.disconnect();

            }
        } catch (org.eclipse.paho.client.mqttv3.MqttException exc) {
            LOGGER.info("error closing MQTT connection", exc);
        }
    }

    public void unsubscribe(String topic) throws MqttException {
        if (mqttSubscriptions.containsKey(topic)) {
            unsubscribeMqtt(topic);
            mqttSubscriptions.get(topic).clear();
            cleanupMqttSubscriptions();
        }
    }

    private void cleanupMqtt() {
        mqttSubscriptions.forEach((x, y) -> {
            try {
                unsubscribe(x);
            } catch (MqttException exc) {
                LOGGER.warn("error unsubscribing from MQTT", exc);
            }
        });
        if (mqttClient != null) {
            try {
                mqttClient.close(true);
            } catch (org.eclipse.paho.client.mqttv3.MqttException ex) {
                LOGGER.warn("error closing MQTT conection");
            }
        }
        mqttClient = null;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (mqttSubscriptions.containsKey(topic)) {
            mqttSubscriptions.get(topic).forEach(x -> x.accept(message));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public SensorThingsAPIVersion getVersion() {
        return version;
    }

}
