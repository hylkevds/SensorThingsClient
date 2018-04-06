package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractSensorBuilder;

/**
 * Default {@link de.fraunhofer.iosb.ilt.sta.model.Sensor} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class SensorBuilder extends AbstractSensorBuilder<SensorBuilder> {

    private SensorBuilder() {
    }

    public static SensorBuilder builder() {
        return new SensorBuilder();
    }

    @Override
    public SensorBuilder getSelf() {
        return this;
    }

}