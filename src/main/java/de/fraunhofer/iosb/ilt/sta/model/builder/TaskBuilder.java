package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractTaskBuilder;

/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.Task} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Michael Jacoby
 */
public final class TaskBuilder extends AbstractTaskBuilder<TaskBuilder> {

    private TaskBuilder() {
    }

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    @Override
    public TaskBuilder getSelf() {
        return this;
    }

}
