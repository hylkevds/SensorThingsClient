package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for operations with the <i>Thing</i> entity.
 *
 * @author Nils Sommer
 *
 */
public class ThingDao extends BaseDao<Thing> {

    public ThingDao(SensorThingsService service) {
        super(service, Thing.class);
    }
}
