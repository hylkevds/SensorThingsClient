package de.fraunhofer.iosb.ilt.sta.dao;

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
        super(service, Datastream.class);
    }
}
