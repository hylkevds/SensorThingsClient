package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.Sensor;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for the <i>Sensor</i> entity.
 *
 * @author Nils Sommer
 *
 */
public class SensorDao extends BaseDao<Sensor> {

	public SensorDao(SensorThingsService service) {
		super(service, Sensor.class);
	}
}
