package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.Task;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for operations with the <i>Task</i> entity.
 *
 * @author Michael Jacoby
 *
 */
public class TaskDao extends BaseDao<Task> {

    public TaskDao(SensorThingsService service) {
        super(service, Task.class);
    }
}
