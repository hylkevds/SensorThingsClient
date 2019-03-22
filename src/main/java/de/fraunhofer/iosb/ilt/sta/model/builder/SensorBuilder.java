package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractSensorBuilder;
import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.Sensor;
/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.Sensor} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class SensorBuilder extends AbstractSensorBuilder<SensorBuilder> {
    private Sensor sensor;

    public SensorBuilder() {
        sensor = new Sensor();
    }

    public static SensorBuilder builder() {
        return new SensorBuilder();
    }

    @Override
    public SensorBuilder getSelf() {
        return this;
    }

    public void withName(String name) {
        sensor.setName(name);
    }

    public void withDescription(String description) {
        sensor.setDescription(description);
    }

    public void withEncodingType(String encodingType) {
        sensor.setEncodingType(encodingType);
    }

    public void withMetadata(Object metadata) {
        sensor.setMetadata(metadata);
    }

    public void withId(long id) {
        sensor.setId(new IdLong(id));
    }

    public void aDefaultSensor() {
        sensor.setId(new IdLong(1l));
        sensor.setName("defaultSensor");
        sensor.setDescription("defaultDescription");
        sensor.setEncodingType("default");
        sensor.setMetadata("defaultMetadata");
    }

    public Sensor build() {
        Sensor rsensor = new Sensor(sensor.getName(), sensor.getDescription(), sensor.getEncodingType(), sensor.getMetadata());
        rsensor.setId(sensor.getId());
        return rsensor;
    }
}
