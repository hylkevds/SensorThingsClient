package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import com.fasterxml.jackson.annotation.JsonValue;
import de.fraunhofer.iosb.ilt.sta.model.MultiDatastream;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
import de.fraunhofer.iosb.ilt.sta.model.Sensor;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import org.geojson.Polygon;
import org.threeten.extra.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for any {@link AbstractEntityBuilder} of {@link MultiDatastream}
 *
 * @param <U> the type of the concrete class that extends this {@link AbstractMultiDatastreamBuilder}
 * @author Aurelien Bourdon
 */
public abstract class AbstractMultiDatastreamBuilder<U extends AbstractMultiDatastreamBuilder<U>> extends AbstractEntityBuilder<MultiDatastream, U> {

    @Override
    protected MultiDatastream newBuildingInstance() {
        return new MultiDatastream();
    }

    @Override
    public MultiDatastream build() {
        // A MultiDatastream is always set by the ValueCode#OM_ComplexObservation observation type
        getBuildingInstance().setObservationType(ValueCode.OM_ComplexObservation.getValue());
        return super.build();
    }

    @Override
    protected MultiDatastream getBuildingInstance() {
        return new MultiDatastream();
    }

    public U name(final String name) {
        getBuildingInstance().setName(name);
        return getSelf();
    }

    public U description(final String description) {
        getBuildingInstance().setDescription(description);
        return getSelf();
    }

    public U unitOfMeasurements(final List<UnitOfMeasurement> unitOfMeasurements) {
        getBuildingInstance().setUnitOfMeasurements(unitOfMeasurements);
        return getSelf();
    }

    public U unitOfMeasurement(final UnitOfMeasurement unitOfMeasurement) {
        if (getBuildingInstance().getUnitOfMeasurements() == null) {
            getBuildingInstance().setUnitOfMeasurements(new ArrayList<>());
        }
        getBuildingInstance().getUnitOfMeasurements().add(unitOfMeasurement);
        return getSelf();
    }

    public U observedArea(final Polygon observedArea) {
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

    public U multiObservationDataTypes(final List<AbstractDatastreamBuilder.ValueCode> valueCodes) {
        getBuildingInstance().setMultiObservationDataTypes(
                valueCodes
                        .stream()
                        .map(AbstractDatastreamBuilder.ValueCode::getValue)
                        .collect(Collectors.toList())
        );
        return getSelf();
    }

    public U multiObservationDataType(final AbstractDatastreamBuilder.ValueCode valueCode) {
        if (getBuildingInstance().getMultiObservationDataTypes() == null) {
            getBuildingInstance().setMultiObservationDataTypes(new ArrayList<>());
        }
        getBuildingInstance().getMultiObservationDataTypes().add(valueCode.getValue());
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

    public U observedProperties(final List<ObservedProperty> observedProperties) {
        getBuildingInstance().getObservedProperties().addAll(observedProperties);
        return getSelf();
    }

    public U observedProperty(final ObservedProperty observedProperty) {
        getBuildingInstance().getObservedProperties().add(observedProperty);
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

    /**
     * All the possible values for a {@link MultiDatastream#observationType} attribute
     *
     * @author Aurelien Bourdon
     */
    public enum ValueCode {
        OM_ComplexObservation("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_ComplexObservation");

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
