package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for the <i>ObservedProperty</i> entity.
 *
 * @author Nils Sommer
 *
 */
public class ObservedPropertyDao extends BaseDao<ObservedProperty> {

	public ObservedPropertyDao(SensorThingsService service) {
		super(service, ObservedProperty.class);
	}
}
