package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.Actuator;
import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractActuatorBuilder;

public class ActuatorBuilder extends AbstractActuatorBuilder<ActuatorBuilder> {

    private Actuator actuator;

    public ActuatorBuilder() {
        actuator = new Actuator();
    }

    public static ActuatorBuilder builder() {
        return new ActuatorBuilder();
    }

    /**
     * Get the reference to the concrete instance that extends this
     * ExtensibleBuilder. Commonly, {@code this}.
     *
     * @return the reference to the concrete instance that extends this
     * ExtensibleBuilder. Commonly, {@code this}.
     */
    @Override
    protected ActuatorBuilder getSelf() {
        return this;
    }

    public void withName(String name) {
        actuator.setName(name);
    }

    public void withDescription(String description) {
        actuator.setDescription(description);
    }

    public void withEncodingType(String encodingType) {
        actuator.setEncodingType(encodingType);
    }

    public void withMetadata(Object metadata) {
        actuator.setMetadata(metadata);
    }

    public void withId(long id) {
        actuator.setId(new IdLong(id));
    }

    public void aDefaultActuator() {
        actuator.setId(new IdLong(1l));
        actuator.setName("defaultActuator");
        actuator.setDescription("defaultDescription");
        actuator.setEncodingType("default");
        actuator.setMetadata("defaultMetadata");
    }

    public Actuator build() {
        Actuator newActuator = new Actuator(actuator.getName(), actuator.getDescription(), actuator.getEncodingType(), actuator.getMetadata());
        actuator.setId(actuator.getId());
        return newActuator;
    }
}
