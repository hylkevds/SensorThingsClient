package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Actuator;
import de.fraunhofer.iosb.ilt.sta.model.TaskingCapability;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for any {@link EntityBuilder} of {@link Actuator}
 *
 * @param <U> the type of the concrete class that extends this
 * {@link AbstractActuatorBuilder}
 * @author Michael Jacoby
 */
public abstract class AbstractActuatorBuilder<U extends AbstractActuatorBuilder<U>> extends EntityBuilder<Actuator, U> {

    @Override
    protected Actuator newBuildingInstance() {
        return new Actuator();
    }

    public U name(final String name) {
        getBuildingInstance().setName(name);
        return getSelf();
    }

    public U description(final String description) {
        getBuildingInstance().setDescription(description);
        return getSelf();
    }

    public U encodingType(final String encodingType) {
        getBuildingInstance().setEncodingType(encodingType);
        return getSelf();
    }

    public U metadata(final Object metadata) {
        getBuildingInstance().setMetadata(metadata);
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

    public U taskingCapabilities(final List<TaskingCapability> taskingCapabilities) {
        getBuildingInstance().getTaskingCapabilities().addAll(taskingCapabilities);
        return getSelf();
    }

    public U taskingCapability(final TaskingCapability taskingCapability) {
        getBuildingInstance().getTaskingCapabilities().add(taskingCapability);
        return getSelf();
    }
}
