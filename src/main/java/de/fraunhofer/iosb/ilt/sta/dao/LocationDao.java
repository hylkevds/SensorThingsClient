package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.Location;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for the <i>Location</i> entity.
 *
 * @author Nils Sommer
 *
 */
public class LocationDao extends BaseDao<Location> {

	public LocationDao(SensorThingsService service) {
		super(service, Location.class);
	}
}
