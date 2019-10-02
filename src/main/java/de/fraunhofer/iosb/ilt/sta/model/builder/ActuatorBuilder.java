package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractActuatorBuilder;

/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.Actuator} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Michael Jacoby
 */
public final class ActuatorBuilder extends AbstractActuatorBuilder<ActuatorBuilder> {

    private ActuatorBuilder() {
    }

    public static ActuatorBuilder builder() {
        return new ActuatorBuilder();
    }

    @Override
    public ActuatorBuilder getSelf() {
        return this;
    }

}
