package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Task;
import de.fraunhofer.iosb.ilt.sta.model.TaskingCapability;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for any {@link EntityBuilder} of {@link Task}
 *
 * @param <U> the type of the concrete class that extends this
 * {@link AbstractTaskBuilder}
 * @author Michael Jacoby
 */
public abstract class AbstractTaskBuilder<U extends AbstractTaskBuilder<U>> extends EntityBuilder<Task, U> {

    @Override
    protected Task newBuildingInstance() {
        return new Task();
    }

    public U creationTime(final ZonedDateTime creationTime) {
        getBuildingInstance().setCreationTime(creationTime);
        return getSelf();
    }

    public U taskingParameters(final Map<String, Object> taskingParameters) {
        getBuildingInstance().setTaskingParameters(taskingParameters);
        return getSelf();
    }

    public U taskingParameter(final String key, final Object value) {
        if (getBuildingInstance().getTaskingParameters() == null) {
            getBuildingInstance().setTaskingParameters(new HashMap<>());
        }
        getBuildingInstance().getTaskingParameters().put(key, value);
        return getSelf();
    }

    public U taskingCapability(final TaskingCapability taskingCapability) {
        getBuildingInstance().setThing(taskingCapability);
        return getSelf();
    }
}
