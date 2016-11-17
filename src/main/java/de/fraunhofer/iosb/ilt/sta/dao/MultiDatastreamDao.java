package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.MultiDatastream;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for the <i>Datastream</i> entity.
 *
 * @author Nils Sommer
 *
 */
public class MultiDatastreamDao extends BaseDao<MultiDatastream> {

	public MultiDatastreamDao(SensorThingsService service) {
		super(service, MultiDatastream.class);
	}
}
