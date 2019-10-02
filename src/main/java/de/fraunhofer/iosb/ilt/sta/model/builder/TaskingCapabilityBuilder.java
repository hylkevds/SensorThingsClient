package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractTaskingCapabilityBuilder;

/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.TaskingCapability} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Michael Jacoby
 */
public final class TaskingCapabilityBuilder extends AbstractTaskingCapabilityBuilder<TaskingCapabilityBuilder> {

    private TaskingCapabilityBuilder() {
    }

    public static TaskingCapabilityBuilder builder() {
        return new TaskingCapabilityBuilder();
    }

    @Override
    public TaskingCapabilityBuilder getSelf() {
        return this;
    }

}
