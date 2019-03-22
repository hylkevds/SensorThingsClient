package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.TaskDao;
import de.fraunhofer.iosb.ilt.sta.dao.TaskingCapabilityDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TaskingCapability extends Entity<TaskingCapability> {

    private String name;
    private String description;
    private Map<String, Object> properties;
    private Map<String, Object> taskingParameters;

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Observation.class);

    @JsonProperty("Thing")
    private Thing thing;

    @JsonProperty("Actuator")
    private Actuator actuator;

    @JsonProperty("Task")
    private EntityList<Task> tasks = new EntityList<>(EntityType.TASKS);

    public TaskingCapability() {
        super(EntityType.TASKING_CAPABILITY);
        taskingParameters = new HashMap<>();
    }

    public TaskingCapability(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public TaskingCapability(String name, String description, Map<String, Object> properties) {
        this(name, description);
        this.properties = properties;
    }

    @Override
    protected void ensureServiceOnChildren(SensorThingsService service) {
        if (actuator != null) {
            actuator.setService(service);
        }
        if (thing != null) {
            thing.setService(service);
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
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + Objects.hashCode(this.properties);
        hash = 97 * hash + Objects.hashCode(this.taskingParameters);
        return hash;
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

    public Map<String, Object> getTaskingParameters() {
        return taskingParameters;
    }

    public void setTaskingParameters(Map<String, Object> taskingParameters) {
        this.taskingParameters = taskingParameters;
    }

    public Actuator getActuator() {
        return this.actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    public EntityList<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(EntityList<Task> tasks) {
        this.tasks = tasks;
    }

    public BaseDao<Task> tasks() {
        return getService().tasks().setParent(this);
    }

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    /**
     * Adds a task to the capability
     * @param parameter is the tasking parameter of the task. Can be null.
     */
    public Task createTask(Map<String, Object> parameter) {
        Task newTask = new Task(this);
        if(parameter != null) {
            newTask.setTaskingParameters(parameter);
        }
        newTask.setService(this.getService());
        return newTask;
    }

    @Override
    public TaskingCapabilityDao getDao(SensorThingsService service) {
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
        return super.toString() + " " + getName();
    }
}
