package de.fraunhofer.iosb.ilt.sta.service;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.DatastreamDao;
import de.fraunhofer.iosb.ilt.sta.dao.FeatureOfInterestDao;
import de.fraunhofer.iosb.ilt.sta.dao.HistoricalLocationDao;
import de.fraunhofer.iosb.ilt.sta.dao.LocationDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservationDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservedPropertyDao;
import de.fraunhofer.iosb.ilt.sta.dao.SensorDao;
import de.fraunhofer.iosb.ilt.sta.dao.ThingDao;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * A SensorThingsService represents the service endpoint of a server.
 *
 * @author Nils Sommer, Hylke van der Schaaf
 */
public class SensorThingsService {

	private final URI endpoint;
	private RequestConfig config = null;
	private CloseableHttpClient client;

	/**
	 * Constructor.
	 *
	 * @param endpoint the base URI of the SensorThings service
	 * @throws java.net.URISyntaxException when building the final url fails.
	 */
	public SensorThingsService(URL endpoint) throws URISyntaxException {
		this(endpoint, null);
	}

	/**
	 * Constructor.
	 *
	 * @param endpoint the base URI of the SensorThings service
	 * @param config the config for the jersey client
	 * @throws java.net.URISyntaxException when building the final url fails.
	 */
	public SensorThingsService(URL endpoint, RequestConfig config) throws URISyntaxException {
		this.endpoint = new URI(endpoint.toString() + "/").normalize();
		this.config = config;
		this.client = HttpClients.createDefault();
	}

	public URI getEndpoint() {
		return this.endpoint;
	}

	/**
	 * The path to the entity or collection. e.g.:
	 * <ul>
	 * <li>Things</li>
	 * <li>Things(2)/Datastreams</li>
	 * <li>Datastreams(5)/Thing</li>
	 * </ul>
	 *
	 * @param parent The entity holding the relation, can be null.
	 * @param relation The relation or collection to get.
	 * @return
	 */
	public String getPath(Entity<?> parent, EntityType relation) {
		if (parent == null) {
			return relation.getName();
		}
		if (!parent.getType().hasRelationTo(relation)) {
			throw new IllegalArgumentException("Entity of type " + parent.getType() + " has no relation of type " + relation + ".");
		}
		return String.format("%s(%d)/%s", EntityType.listForClass(parent.getClass()).getName(), parent.getId(), relation.getName());
	}

	/**
	 * The full path to the entity or collection.
	 *
	 * @param parent The entity holding the relation, can be null.
	 * @param relation The relation or collection to get.
	 * @return
	 */
	public URI getFullPath(Entity<?> parent, EntityType relation) {
		return getEndpoint().resolve(getPath(parent, relation));
	}

	public CloseableHttpClient getClient() {
		return client;
	}

	public RequestConfig getConfig() {
		return config;
	}

	public DatastreamDao datastreams() {
		return new DatastreamDao(this);
	}

	public FeatureOfInterestDao featuresOfInterest() {
		return new FeatureOfInterestDao(this);
	}

	public HistoricalLocationDao historicalLocations() {
		return new HistoricalLocationDao(this);
	}

	public LocationDao locations() {
		return new LocationDao(this);
	}

	public ObservationDao observations() {
		return new ObservationDao(this);
	}

	public ObservedPropertyDao observedProperties() {
		return new ObservedPropertyDao(this);
	}

	public SensorDao sensors() {
		return new SensorDao(this);
	}

	public ThingDao things() {
		return new ThingDao(this);
	}

	public <T extends Entity> void create(T entity) throws ServiceFailureException {
		entity.getDao(this).create(entity);
	}

	public <T extends Entity> void delete(T entity) throws ServiceFailureException {
		entity.getDao(this).delete(entity);
	}
}
