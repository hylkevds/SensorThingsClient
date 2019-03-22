package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Actuator;
import de.fraunhofer.iosb.ilt.sta.model.Task;
import de.fraunhofer.iosb.ilt.sta.model.TaskingCapability;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;

public abstract class AbstractTaskingCapabilityBuilder<U extends AbstractTaskingCapabilityBuilder<U>> extends EntityBuilder<TaskingCapability, U> {

    /**
     * Create the new instance that will be build by this
     * {@link AbstractBuilder}
     *
     * @return the new instance that will be build by this
     * {@link AbstractBuilder}
     */
    @Override
    protected TaskingCapability newBuildingInstance() {
        return new TaskingCapability();
    }

    public U name(final String name) {
        getBuildingInstance().setName(name);
        return getSelf();
    }

    public U description(final String description) {
        getBuildingInstance().setDescription(description);
        return getSelf();
    }

    public U actuator(final Actuator actuator) {
        getBuildingInstance().setActuator(actuator);
        return getSelf();
    }

    public U task(final Task task) {
        getBuildingInstance().getTasks().add(task);
        return getSelf();
    }

    public U tasks(final EntityList<Task> tasks) {
        getBuildingInstance().getTasks().addAll(tasks);
        return getSelf();
    }

    public U thing(final Thing thing) {
        getBuildingInstance().setThing(thing);
        return getSelf();
    }
}
