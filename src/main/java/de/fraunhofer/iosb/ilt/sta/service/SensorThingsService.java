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
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * A SensorThingsService represents the service endpoint of a server.
 *
 * @author Nils Sommer
 */
public class SensorThingsService {

	private final URI endpoint;
	private RequestConfig config = null;

	/**
	 * Constructor.
	 *
	 * @param endpoint the base URI of the SensorThings service
	 */
	public SensorThingsService(URI endpoint) throws URISyntaxException {
		this.endpoint = new URI(endpoint.toString() + "/").normalize();
	}

	/**
	 * Constructor.
	 *
	 * @param endpoint the base URI of the SensorThings service
	 * @param config the config for the jersey client
	 */
	public SensorThingsService(URI endpoint, RequestConfig config) throws URISyntaxException {
		this.endpoint = new URI(endpoint.toString() + "/").normalize();
		this.config = config;
	}

	public URI getEndpoint() {
		return this.endpoint;
	}

	public CloseableHttpClient newClient() {
		return HttpClients.createDefault();
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
