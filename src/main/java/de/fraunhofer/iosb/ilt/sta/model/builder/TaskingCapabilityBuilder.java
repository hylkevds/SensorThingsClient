package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.TaskingCapability;
import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractTaskingCapabilityBuilder;

public class TaskingCapabilityBuilder extends AbstractTaskingCapabilityBuilder<TaskingCapabilityBuilder> {
    private TaskingCapability taskingCapability;

    public TaskingCapabilityBuilder() {
        taskingCapability = new TaskingCapability();
    }

    public static TaskingCapabilityBuilder builder() {
        return new TaskingCapabilityBuilder();
    }


    /**
     * Get the reference to the concrete instance that extends this
     * ExtensibleBuilder. Commonly, {@code this}.
     *
     * @return the reference to the concrete instance that extends this
     * ExtensibleBuilder. Commonly, {@code this}.
     */
    @Override
    protected TaskingCapabilityBuilder getSelf() {
        return this;
    }

    public void withName(String name) {
        taskingCapability.setName(name);
    }

    public void withDescription(String description) {
        taskingCapability.setDescription(description);
    }

    public void withId(long id) {
        taskingCapability.setId(new IdLong(id));
    }

    public void aDefaultTaskingCapability() {
        taskingCapability.setId(new IdLong(1l));
        taskingCapability.setName("defaultTaskingCapability");
        taskingCapability.setDescription("default");
    }

    public TaskingCapability build() {
        TaskingCapability newTaskingCapability = new TaskingCapability(taskingCapability.getName(), taskingCapability.getDescription());
        newTaskingCapability.setId(taskingCapability.getId());
        return newTaskingCapability;
    }
}
