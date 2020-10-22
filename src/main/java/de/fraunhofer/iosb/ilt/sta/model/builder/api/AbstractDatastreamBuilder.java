package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import com.fasterxml.jackson.annotation.JsonValue;
import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
import de.fraunhofer.iosb.ilt.sta.model.Sensor;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geojson.GeoJsonObject;
import org.threeten.extra.Interval;

/**
 * Base class for any {@link EntityBuilder} of {@link Datastream}
 *
 * @param <U> the type of the concrete class that extends this
 * {@link AbstractDatastreamBuilder}
 * @author Aurelien Bourdon
 */
public abstract class AbstractDatastreamBuilder<U extends AbstractDatastreamBuilder<U>> extends EntityBuilder<Datastream, U> {

    @Override
    protected Datastream newBuildingInstance() {
        return new Datastream();
    }

    public U name(final String name) {
        getBuildingInstance().setName(name);
        return getSelf();
    }

    public U description(final String description) {
        getBuildingInstance().setDescription(description);
        return getSelf();
    }

    public U observationType(final ValueCode observationType) {
        getBuildingInstance().setObservationType(observationType.getValue());
        return getSelf();
    }

    public U unitOfMeasurement(final UnitOfMeasurement unitOfMeasurement) {
        getBuildingInstance().setUnitOfMeasurement(unitOfMeasurement);
        return getSelf();
    }

    public U observedArea(final GeoJsonObject observedArea) {
        getBuildingInstance().setObservedArea(observedArea);
        return getSelf();
    }

    public U phenomenonTime(final Interval phenomenonTime) {
        getBuildingInstance().setPhenomenonTime(phenomenonTime);
        return getSelf();
    }

    public U resultTime(final Interval resultTime) {
        getBuildingInstance().setResultTime(resultTime);
        return getSelf();
    }

    public U sensor(final Sensor sensor) {
        getBuildingInstance().setSensor(sensor);
        return getSelf();
    }

    public U thing(final Thing thing) {
        getBuildingInstance().setThing(thing);
        return getSelf();
    }

    public U observedProperty(final ObservedProperty observedProperty) {
        getBuildingInstance().setObservedProperty(observedProperty);
        return getSelf();
    }

    public U observations(final List<Observation> observations) {
        getBuildingInstance().getObservations().addAll(observations);
        return getSelf();
    }

    public U observation(final Observation observation) {
        getBuildingInstance().getObservations().add(observation);
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

    /**
     * All the possible values for a {@link Datastream#observationType}
     * attribute
     *
     * @author Aurelien Bourdon
     */
    public enum ValueCode {
        OM_CategoryObservation("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CategoryObservation"),
        OM_CountObservation("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation"),
        OM_Measurement("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement"),
        OM_Observation("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Observation"),
        OM_TruthObservation("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation");

        private final String value;

        ValueCode(final String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

}
