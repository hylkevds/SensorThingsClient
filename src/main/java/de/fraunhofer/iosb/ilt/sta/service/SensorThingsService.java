package de.fraunhofer.iosb.ilt.sta.service;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.DatastreamDao;
import de.fraunhofer.iosb.ilt.sta.dao.FeatureOfInterestDao;
import de.fraunhofer.iosb.ilt.sta.dao.HistoricalLocationDao;
import de.fraunhofer.iosb.ilt.sta.dao.LocationDao;
import de.fraunhofer.iosb.ilt.sta.dao.MultiDatastreamDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservationDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservedPropertyDao;
import de.fraunhofer.iosb.ilt.sta.dao.SensorDao;
import de.fraunhofer.iosb.ilt.sta.dao.ThingDao;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.ext.DataArrayDocument;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * A SensorThingsService represents the service endpoint of a server.
 *
 * @author Nils Sommer, Hylke van der Schaaf
 */
public class SensorThingsService {

	private URI endpoint;
	private CloseableHttpClient client;
	private TokenManager tokenManager;

	/**
	 * Creates a new SensorThingsService without an endpoint url set. The
	 * endpoint url MUST be set before the service can be used.
	 */
	public SensorThingsService() {
		this.client = HttpClients.createSystem();
	}

	/**
	 * Constructor.
	 *
	 * @param endpoint the base URI of the SensorThings service
	 * @throws java.net.URISyntaxException when building the final url fails.
	 */
	public SensorThingsService(URL endpoint) throws URISyntaxException {
		this.endpoint = new URI(endpoint.toString() + "/").normalize();
		this.client = HttpClients.createSystem();
	}

	/**
	 * Sets the endpoint URI. Once the endpoint URI is set it can not be
	 * changed. The endpoint url MUST be set before the service can be used.
	 *
	 * @param endpoint The URL of the endpoint.
	 * @throws java.net.URISyntaxException when building the final url fails.
	 */
	public void setEndpoint(URI endpoint) throws URISyntaxException {
		if (this.endpoint != null) {
			throw new IllegalStateException("endpoint URI already set.");
		}
		this.endpoint = new URI(endpoint.toString() + "/").normalize();
	}

	public URI getEndpoint() {
		if (this.endpoint == null) {
			throw new IllegalStateException("endpoint URI not set.");
		}
		return this.endpoint;
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
	 * The full path to the entity or collection.
	 *
	 * @param parent The entity holding the relation, can be null.
	 * @param relation The relation or collection to get.
	 * @return the full path to the entity or collection.
	 */
	public URI getFullPath(Entity<?> parent, EntityType relation) {
		return getEndpoint().resolve(getPath(parent, relation));
	}

	/**
	 * Execute the given request, adding a token header if needed.
	 *
	 * @param request The request to execute.
	 * @return the response.
	 * @throws IOException in case of problems.
	 */
	public CloseableHttpResponse execute(HttpUriRequest request) throws IOException {
		if (tokenManager != null) {
			tokenManager.addAuthHeader(request);
		}
		return client.execute(request);
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
			tokenManager.setHttpClient(client);
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
	public CloseableHttpClient getClient() {
		return client;
	}

	/**
	 * Set the httpclient used for requests.
	 *
	 * @param client the client to set
	 */
	public void setClient(CloseableHttpClient client) {
		this.client = client;
	}

}
