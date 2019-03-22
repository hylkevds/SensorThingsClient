package de.fraunhofer.iosb.ilt.sta.model.builder;

import java.io.IOException;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractDatastreamBuilder;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
import de.fraunhofer.iosb.ilt.sta.model.*;
/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.Datastream Datastream}
 * {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder Builder}
 *
 * @author Aurelien Bourdon
 */
public final class DatastreamBuilder extends AbstractDatastreamBuilder<DatastreamBuilder> {
    private Datastream datastream;

    private DatastreamBuilder() {
            datastream = new Datastream();
    }

    public static DatastreamBuilder builder() {
        return new DatastreamBuilder();
    }

    @Override
    public DatastreamBuilder getSelf() {
        return this;
    }


    public void withName(String name) {
        datastream.setName(name);
    }

    public void withDescription(String description) {
        datastream.setDescription(description);
    }

    public void withObservationType(String observationType) {
        datastream.setObservationType(observationType);
    }

    public void withUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) {
        datastream.setUnitOfMeasurement(unitOfMeasurement);
    }

    public void withObservedProperty(ObservedProperty observedProperty) {
        datastream.setObservedProperty(observedProperty);
    }

    public void withSensor(Sensor sensor) {
        datastream.setSensor(sensor);
    }

    public void withThing(Thing thing) {
        datastream.setThing(thing);
    }

    public void withId(long id) {
        datastream.setId(new IdLong(id));
    }

    public void aDefaultDatastream() throws IOException {
        datastream.setName("defaultDatastream");
        datastream.setDescription("defaultDescription");
        datastream.setObservationType("defaultObservationType");
        datastream.setUnitOfMeasurement(new UnitOfMeasurement("defaultUnit", "defaultSym", "defaultDef"));
        ObservedPropertyBuilder observedPropertyBuilder = new ObservedPropertyBuilder();
        observedPropertyBuilder.aDefaultObservedProperty();
        datastream.setObservedProperty(observedPropertyBuilder.build());
        SensorBuilder sensorBuilder = new SensorBuilder();
        sensorBuilder.aDefaultSensor();
        datastream.setSensor(sensorBuilder.build());
        ThingBuilder thingBuilder = new ThingBuilder();
        thingBuilder.aDefaultThing();
        datastream.setThing(thingBuilder.build());
        datastream.setId(new IdLong(1l));
    }

    public Datastream build() { //throws ServiceFailureException {
        Datastream rDatastream = new Datastream(datastream.getName(),
                datastream.getDescription(),
                datastream.getObservationType(),
                datastream.getUnitOfMeasurement());
        try {
            rDatastream.setObservedProperty(datastream.getObservedProperty());
        }
        catch (ServiceFailureException sfe) {
	        System.err.println("Observed property couldn't be fetched.");
        }

        try {
            rDatastream.setSensor(datastream.getSensor());
        }
        catch (ServiceFailureException sfe) {
	        System.err.println("Sensor couldn't be fetched.");
        }

        try {
            rDatastream.setThing(datastream.getThing());
        }
        catch (ServiceFailureException sfe) {
	        System.err.println("Thing couldn't be fetched.");
        }
        rDatastream.setId(datastream.getId());
        return rDatastream;
    }
}
