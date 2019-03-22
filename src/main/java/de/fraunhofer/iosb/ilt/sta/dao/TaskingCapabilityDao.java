package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.TaskingCapability;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for operations with the <i>TaskingCapability</i> entity.
 *
 */
public class TaskingCapabilityDao extends BaseDao<TaskingCapability> {

    public TaskingCapabilityDao(SensorThingsService service) {
        super(service, TaskingCapability.class);
    }
}
