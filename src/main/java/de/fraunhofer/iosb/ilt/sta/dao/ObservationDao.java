package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for the <i>Observation</i> entity.
 *
 * @author Nils Sommer
 *
 */
public class ObservationDao extends BaseDao<Observation> {

	public ObservationDao(SensorThingsService service) {
		super(service, Observation.class);
	}
}
