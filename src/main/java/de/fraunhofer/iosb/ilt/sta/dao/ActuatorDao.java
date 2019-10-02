package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.Actuator;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for operations with the <i>Actuator</i> entity.
 *
 * @author Michael Jacoby
 *
 */
public class ActuatorDao extends BaseDao<Actuator> {

    public ActuatorDao(SensorThingsService service) {
        super(service, Actuator.class);
    }
}
