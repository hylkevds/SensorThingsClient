package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.model.parameterTypes.TaskingParameterType;

public class TaskingParameter<T extends TaskingParameterType> {
    private String taskName;
    private T parameter;
    private TaskState state;

    @JsonProperty("Task")
    private Task task;

    public TaskingParameter(T parameter) {
        this.parameter = parameter;
        this.state = TaskState.NEW;
    }

    public TaskingParameter(String taskName, T parameter) {
        this.taskName = taskName;
        this.parameter = parameter;
        this.state = TaskState.NEW;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public T getParameter() {
        return this.parameter;
    }

    public TaskState getState() {
        return this.state;
    }

    public String getTaskName() {
        return this.taskName;
    }

}
