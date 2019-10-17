package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.TaskingCapabilityDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import de.fraunhofer.iosb.ilt.swe.common.complex.DataRecord;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Michael Jacoby
 */
public class TaskingCapability extends Entity<TaskingCapability> {

    private String name;
    private String description;
    private Map<String, Object> properties;
    private DataRecord taskingParameters;

    private final EntityList<Task> tasks = new EntityList<>(EntityType.TASKS);
    @JsonProperty("Thing")
    private Thing thing;

    @JsonProperty("Actuator")
    private Actuator actuator;

    public TaskingCapability() {
        super(EntityType.TASK);
    }

    @Override
    protected void ensureServiceOnChildren(SensorThingsService service) {
        if (actuator != null) {
            actuator.setService(service);
        }
        if (thing != null) {
            thing.setService(service);
        }
        if (tasks != null) {
            tasks.setService(service, Task.class);
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
        final TaskingCapability other = (TaskingCapability) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.properties, other.properties)) {
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
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + Objects.hashCode(this.description);
        hash = 59 * hash + Objects.hashCode(this.properties);
        hash = 59 * hash + Objects.hashCode(this.taskingParameters);
        return hash;
    }

//    @JsonSerialize(using = TaskingParameterListSerializer.class)
    public DataRecord getTaskingParameters() {
        return this.taskingParameters;
    }

    public void setTaskingParameters(DataRecord taskingParameters) {
        this.taskingParameters = taskingParameters;
    }

    @Override
    public BaseDao<TaskingCapability> getDao(SensorThingsService service) {
        return new TaskingCapabilityDao(service);
    }

    @Override
    public TaskingCapability withOnlyId() {
        TaskingCapability copy = new TaskingCapability();
        copy.setId(id);
        return copy;
    }

    @Override
    public String toString() {
        return super.toString() + " [" + name + "] " + taskingParameters;
    }

    public Thing getThing() throws ServiceFailureException {
        if (thing == null && getService() != null) {
            thing = getService().things().find(this);
        }
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    public Actuator getActuator() throws ServiceFailureException {
        if (actuator == null && getService() != null) {
            actuator = getService().actuators().find(this);
        }
        return actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    @JsonProperty("Tasks")
    public EntityList<Task> getTasks() {
        return this.tasks;
    }

    @JsonProperty("Tasks")
    public void setTasks(List<Task> tasks) {
        this.tasks.replaceAll(tasks);
    }

    public BaseDao<Task> tasks() {
        return getService().tasks().setParent(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

}
