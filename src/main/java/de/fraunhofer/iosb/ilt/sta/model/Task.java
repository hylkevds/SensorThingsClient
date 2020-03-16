package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.TaskDao;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Michael Jacoby
 */
public class Task extends Entity<Task> {

    private ZonedDateTime creationTime;
    private Map<String, Object> taskingParameters;

    @JsonProperty("TaskingCapability")
    private TaskingCapability taskingCapability;

    public Task() {
        super(EntityType.TASK);
        this.taskingParameters = new HashMap<>();
    }

    public Task(ZonedDateTime creationTime, Map<String, Object> taskingParameters) {
        this();
        this.creationTime = creationTime;
        this.taskingParameters = taskingParameters;
    }

    @Override
    protected void ensureServiceOnChildren(SensorThingsService service) {
        if (taskingCapability != null) {
            taskingCapability.setService(service);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        if (!Objects.equals(this.creationTime, other.creationTime)) {
            return false;
        }
        if (!Objects.equals(this.taskingParameters, other.taskingParameters)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 59 * hash + Objects.hashCode(this.creationTime);
        hash = 59 * hash + Objects.hashCode(this.taskingParameters);
        return hash;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }
    
    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public Map<String, Object> getTaskingParameters() {
        return this.taskingParameters;
    }

    public void setTaskingParameters(Map<String, Object> taskingParameters) {
        this.taskingParameters = taskingParameters;
    }

    public TaskingCapability getTaskingCapability() throws ServiceFailureException {
        if (taskingCapability == null && getService() != null) {
            taskingCapability = getService().taskingCapabilities().find(this);
        }
        return taskingCapability;
    }

    public void setTaskingCapability(TaskingCapability taskingCapability) {
        this.taskingCapability = taskingCapability;
    }

    @Override
    public BaseDao<Task> getDao(SensorThingsService service) {
        return new TaskDao(service);
    }

    @Override
    public Task withOnlyId() {
        Task copy = new Task();
        copy.setId(id);
        return copy;
    }

    @Override
    public String toString() {
        return super.toString() + " [" + creationTime + "] " + taskingParameters;
    }

}
