package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.Task;
import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractTaskBuilder;

import java.util.Map;

public class TaskBuilder extends AbstractTaskBuilder<TaskBuilder> {
    private Task task;

    public TaskBuilder() {
        task = new Task();
    }

    /**
     * Get the reference to the concrete instance that extends this
     * ExtensibleBuilder. Commonly, {@code this}.
     *
     * @return the reference to the concrete instance that extends this
     * ExtensibleBuilder. Commonly, {@code this}.
     */
    @Override
    protected TaskBuilder getSelf() {
        return this;
    }

    public void withId(long id) {
        task.setId(new IdLong(id));
    }

    public void withParameters(Map<String, Object> parameters) {
        task.setTaskingParameters(parameters);
    }

    public void aDefaultTask() {
        task.setId(new IdLong(1l));
    }

    public Task build() {
        try {
            Task newTask = new Task(task.getTaskingCapability(), task.getTaskingParameters());
            newTask.setId(task.getId());
            return newTask;
        } catch (ServiceFailureException e) {
            Task newTask = new Task();
            newTask.setId(task.getId());
            return newTask;
        }
    }
}
