package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Actuator;
import de.fraunhofer.iosb.ilt.sta.model.Task;
import de.fraunhofer.iosb.ilt.sta.model.TaskingCapability;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.swe.common.AbstractDataComponent;
import de.fraunhofer.iosb.ilt.swe.common.complex.DataRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for any {@link EntityBuilder} of {@link TaskingCapability}
 *
 * @param <U> the type of the concrete class that extends this
 * {@link AbstractTaskingCapabilityBuilder}
 * @author Michael Jacoby
 */
public abstract class AbstractTaskingCapabilityBuilder<U extends AbstractTaskingCapabilityBuilder<U>> extends EntityBuilder<TaskingCapability, U> {

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

    public U properties(final Map<String, Object> properties) {
        getBuildingInstance().setProperties(properties);
        return getSelf();
    }

    public U property(final String key, final Object value) {
        if (getBuildingInstance().getProperties() == null) {
            getBuildingInstance().setProperties(new HashMap<>());
        }
        getBuildingInstance().getProperties().put(key, value);
        return getSelf();
    }

    public U taskingParameters(final DataRecord taskingParameters) {
        getBuildingInstance().setTaskingParameters(taskingParameters);
        return getSelf();
    }

    public U taskingParameter(AbstractDataComponent taskingParameter) {
        if (getBuildingInstance().getTaskingParameters() == null) {
            getBuildingInstance().setTaskingParameters(new DataRecord());
        }
        getBuildingInstance().getTaskingParameters().getFields().add(taskingParameter);
        return getSelf();
    }

    public U actuator(final Actuator actuator) {
        getBuildingInstance().setActuator(actuator);
        return getSelf();
    }

    public U thing(final Thing thing) {
        getBuildingInstance().setThing(thing);
        return getSelf();
    }

    public U task(final Task task) {
        getBuildingInstance().getTasks().add(task);
        return getSelf();
    }

    public U tasks(final List<Task> tasks) {
        getBuildingInstance().getTasks().addAll(tasks);
        return getSelf();
    }

}
