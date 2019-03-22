package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.*;

import java.util.List;

public abstract class AbstractActuatorBuilder<U extends AbstractActuatorBuilder<U>> extends EntityBuilder<Actuator, U>{

    /**
     * Create the new instance that will be build by this
     * {@link AbstractBuilder}
     *
     * @return the new instance that will be build by this
     * {@link AbstractBuilder}
     */
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

    public U encodingType(final ValueCode encodingType) {
        getBuildingInstance().setEncodingType(encodingType.getValue());
        return getSelf();
    }

    public U metadata(final Object metadata) {
        getBuildingInstance().setMetadata(metadata);
        return getSelf();
    }

    public U taskingCapabilities(final List<TaskingCapability> capabilities) {
        getBuildingInstance().getCapabilities().addAll(capabilities);
        return getSelf();
    }

    public U taskingCapability(final TaskingCapability capability) {
        getBuildingInstance().getCapabilities().add(capability);
        return getSelf();
    }

    public U id(final Id number) {
        getBuildingInstance().setNumber(number);
        return getSelf();
    }

    /**
     * All the possible values for a Actuator encodingType attribute
     *
     * @author Aurelien Bourdon
     */
    public enum ValueCode {

        PDF("application/pdf"),
        SensorML("http://www.opengis.net/doc/IS/SensorML/2.0");
        private final String value;

        ValueCode(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
}
