package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.MultiDatastream;
import de.fraunhofer.iosb.ilt.sta.model.Sensor;

import java.util.List;

/**
 * Base class for any {@link EntityBuilder} of {@link Sensor}
 *
 * @param <U> the type of the concrete class that extends this {@link AbstractSensorBuilder}
 * @author Aurelien Bourdon
 */
public abstract class AbstractSensorBuilder<U extends AbstractSensorBuilder<U>> extends EntityBuilder<Sensor, U> {

    @Override
    protected Sensor newBuildingInstance() {
        return new Sensor();
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

    public U datastreams(final List<Datastream> datastreams) {
        getBuildingInstance().getDatastreams().addAll(datastreams);
        return getSelf();
    }

    public U datastream(final Datastream datastream) {
        getBuildingInstance().getDatastreams().add(datastream);
        return getSelf();
    }

    public U multiDatastreams(final List<MultiDatastream> multiDatastreams) {
        getBuildingInstance().getMultiDatastreams().addAll(multiDatastreams);
        return getSelf();
    }

    public U multiDatastream(final MultiDatastream multiDatastream) {
        getBuildingInstance().getMultiDatastreams().add(multiDatastream);
        return getSelf();
    }

    /**
     * All the possible values for a {@link Sensor#encodingType} attribute
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
