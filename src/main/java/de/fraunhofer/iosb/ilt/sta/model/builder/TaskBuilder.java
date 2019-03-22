package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.Task;
import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractTaskBuilder;

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

    public void aDefaultTask() {

    }

    public Task build() {
        Task newTask = new Task();
        return newTask;
    }
}
