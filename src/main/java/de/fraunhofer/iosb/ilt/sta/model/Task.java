package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.TaskDao;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

import java.time.LocalDateTime;
import de.fraunhofer.iosb.ilt.sta.NotFoundException;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class Task extends Entity<Task> {

    private Map<String, Object> taskingParameters;
    private LocalDateTime creationTime;

    @JsonProperty(value = "id")
    private Id number;

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Observation.class);

    @JsonProperty("TaskingCapability")
    private TaskingCapability taskingCapability = null;

    public Task() {
        super(EntityType.TASK);
        this.creationTime = LocalDateTime.now();
    }

    public Task(TaskingCapability capability) {
        this();
        this.taskingCapability = capability;
    }

    public Task(TaskingCapability capability, Map<String, Object> parameter) {
        this(capability);
        this.taskingParameters = parameter;
    }

    public Map<String, Object> getTaskingParameters() {
        return taskingParameters;
    }

    public void setTaskingParameters(Map<String, Object> taskingParameters) {
        this.taskingParameters = taskingParameters;
    }

    /*public void addTaskingParameter(TaskingParameter parameter) {
        taskingParameters.add(parameter);
    }*/

    /*/*
     * Sets the state of a task. The Task's state is hold in the parameters.
     * @param state the new state of the task
     */
    /*public void setState(TaskState state) {
        if(taskingParameters.containsKey("state")) {
            taskingParameters.put("state", state);
        } else {
            taskingParameters.replace("state", state);
        }
    }*/

    public Id getNumber() {
        return number;
    }

    public void setNumber(Id number) {
        this.number = number;
    }

    @Override
    protected void ensureServiceOnChildren (SensorThingsService service){
        if (taskingCapability != null) {
            taskingCapability.setService(service);
        }
    }

    @Override
    public boolean equals (Object obj){
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
        if (!Objects.equals(this.taskingParameters, other.taskingParameters)) {
            return false;
        }
        if (!Objects.equals(this.creationTime, other.creationTime)) {
            return false;
        }
        if (!Objects.equals(this.taskingCapability, other.taskingCapability)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode () {
        int hash = super.hashCode();
        hash = 89 * hash + Objects.hashCode(this.taskingParameters);
        hash = 89 * hash + Objects.hashCode(this.creationTime);
        hash = 89 * hash + Objects.hashCode(this.taskingCapability);
        return hash;
    }

    public LocalDateTime getCreationTime() {
        return this.creationTime;
    }

    public TaskingCapability getTaskingCapability () throws ServiceFailureException {
        if (taskingCapability == null && getService() != null) {
            try {
                taskingCapability = getService().taskingCapabilities().find(this);
            } catch (NotFoundException ex) {
                LOGGER.trace("Task has no TaskingCapability.", ex);
            }
        }
        return taskingCapability;
    }

    @Override
    public BaseDao<Task> getDao (SensorThingsService service){
        return new TaskDao(service);
    }

    /**
     * Creates a copy of the entity, with only the ID field set. Useful when
     * creating a new entity that links to this entity.
     *
     * @return a copy with only the ID field set.
     */
    @Override
    public Task withOnlyId () {
        Task copy = new Task();
        copy.setId(id);
        return copy;
    }

}