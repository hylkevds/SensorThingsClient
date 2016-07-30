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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;

/**
 * A SensorThingsService represents the service endpoint of a server.
 *
 * @author Nils Sommer
 */
public class SensorThingsService {

	private final URI endpoint;
	private ClientConfig config = null;

	/**
	 * Constructor.
	 *
	 * @param endpoint the base URI of the SensorThings service
	 */
	public SensorThingsService(URI endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * Constructor.
	 *
	 * @param endpoint the base URI of the SensorThings service
	 * @param config the config for the jersey client
	 */
	public SensorThingsService(URI endpoint, ClientConfig config) {
		this.endpoint = endpoint;
		this.config = config;
	}

	public URI getEndpoint() {
		return this.endpoint;
	}

	public Client newClient() {
		if (this.config == null) {
			return ClientBuilder.newClient()
					.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
		} else {
			return ClientBuilder.newClient(this.config)
					.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
		}
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

	public <T extends Entity> void delete(T entity) {
		entity.getDao(this).delete(entity);
	}
}
