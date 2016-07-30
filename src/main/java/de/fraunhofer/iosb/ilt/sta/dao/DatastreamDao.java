package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for the <i>Datastream</i> entity.
 *
 * @author Nils Sommer
 *
 */
public class DatastreamDao extends BaseDao<Datastream> {

	public DatastreamDao(SensorThingsService service) {
		super(service, "Datastreams", Datastream.class);
	}

	@Override
	public Datastream find(Long id) throws ServiceFailureException {
		throw new RuntimeException(
				"Due to a bug in threeten-extra, Datastreams can't be deserialized. "
				+ "Solution: Wait until threeten-extra 1.1 is released"
				+ " or temoprarily switch to joda-time.");
	}
}
