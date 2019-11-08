/*
 * Copyright (C) 2016 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
 * Karlsruhe, Germany.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.iosb.fraunhofer.ilt.sta;

import de.fraunhofer.iosb.ilt.sta.MqttException;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.model.Actuator;
import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.EntityProperty;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.FeatureOfInterest;
import de.fraunhofer.iosb.ilt.sta.model.HistoricalLocation;
import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.MultiDatastream;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
import de.fraunhofer.iosb.ilt.sta.model.Sensor;
import de.fraunhofer.iosb.ilt.sta.model.Task;
import de.fraunhofer.iosb.ilt.sta.model.TaskingCapability;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.builder.ActuatorBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.DatastreamBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.FeatureOfInterestBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.HistoricalLocationBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.LocationBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.MultiDatastreamBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.ObservationBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.ObservedPropertyBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.SensorBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.TaskBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.TaskingCapabilityBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.ThingBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractActuatorBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractDatastreamBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractLocationBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractSensorBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.ext.TextBuilder;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import de.fraunhofer.iosb.ilt.sta.service.MqttConfig;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;
import org.geojson.Point;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Michael Jacoby
 */
public class MqttTest {

    private SensorThingsService service;

    @Before
    public void setUp() throws MalformedURLException, MqttException {
        service = new SensorThingsService(new URL("http://localhost:8080/FROST-Server/v1.0"), new MqttConfig("tcp://localhost:1883"));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void subscribeEntityDirect() throws MalformedURLException, MqttException {
        Assert.assertEquals("v1.0/Actuators(1)", ActuatorBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Datastreams(1)", DatastreamBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/FeaturesOfInterest(1)", FeatureOfInterestBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/HistoricalLocations(1)", HistoricalLocationBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/MultiDatastreams(1)", MultiDatastreamBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Observations(1)", ObservationBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/ObservedProperties(1)", ObservedPropertyBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Sensors(1)", SensorBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Tasks(1)", TaskBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/TaskingCapabilities(1)", TaskingCapabilityBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Things(1)", ThingBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {}).getTopic());
    }

    @Test
    public void subscribeEntityRelative() throws MalformedURLException, MqttException {
        Datastream datastream = DatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/Datastreams(1)/ObservedProperty", datastream.subscribeRelative(x -> {}, EntityType.OBSERVED_PROPERTY).getTopic());
        Assert.assertEquals("v1.0/Datastreams(1)/Sensor", datastream.subscribeRelative(x -> {}, EntityType.SENSOR).getTopic());
        Assert.assertEquals("v1.0/Datastreams(1)/Thing", datastream.subscribeRelative(x -> {}, EntityType.THING).getTopic());

        HistoricalLocation historicalLocation = HistoricalLocationBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/HistoricalLocations(1)/Thing", historicalLocation.subscribeRelative(x -> {}, EntityType.THING).getTopic());

        MultiDatastream multiDatastream = MultiDatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/MultiDatastreams(1)/Sensor", multiDatastream.subscribeRelative(x -> {}, EntityType.SENSOR).getTopic());
        Assert.assertEquals("v1.0/MultiDatastreams(1)/Thing", multiDatastream.subscribeRelative(x -> {}, EntityType.THING).getTopic());

        Observation observation = ObservationBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/Observations(1)/Datastream", observation.subscribeRelative(x -> {}, EntityType.DATASTREAM).getTopic());
        Assert.assertEquals("v1.0/Observations(1)/Datastream/ObservedProperty", observation.subscribeRelative(x -> {}, EntityType.DATASTREAM, EntityType.OBSERVED_PROPERTY).getTopic());
        Assert.assertEquals("v1.0/Observations(1)/Datastream/Sensor", observation.subscribeRelative(x -> {}, EntityType.DATASTREAM, EntityType.SENSOR).getTopic());
        Assert.assertEquals("v1.0/Observations(1)/Datastream/Thing", observation.subscribeRelative(x -> {}, EntityType.DATASTREAM, EntityType.THING).getTopic());
        Assert.assertEquals("v1.0/Observations(1)/FeatureOfInterest", observation.subscribeRelative(x -> {}, EntityType.FEATURE_OF_INTEREST).getTopic());

        Task task = TaskBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/Tasks(1)/TaskingCapability", task.subscribeRelative(x -> {}, EntityType.TASKING_CAPABILITY).getTopic());

        TaskingCapability taskingCapability = TaskingCapabilityBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/TaskingCapabilities(1)/Actuator", taskingCapability.subscribeRelative(x -> {}, EntityType.ACTUATOR).getTopic());
        Assert.assertEquals("v1.0/TaskingCapabilities(1)/Thing", taskingCapability.subscribeRelative(x -> {}, EntityType.THING).getTopic());
    }

    @Test
    public void subscribeEntityRelative_Invalid() throws MalformedURLException, MqttException {
        String errorMessage = "invalid relative subscription path";

        Datastream datastream = DatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            datastream.subscribeRelative(x -> {}, EntityType.DATASTREAM);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        HistoricalLocation historicalLocation = HistoricalLocationBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            historicalLocation.subscribeRelative(x -> {}, EntityType.HISTORICAL_LOCATION);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        MultiDatastream multiDatastream = MultiDatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            multiDatastream.subscribeRelative(x -> {}, EntityType.DATASTREAM);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        Observation observation = ObservationBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            observation.subscribeRelative(x -> {}, EntityType.THING);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        Task task = TaskBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            task.subscribeRelative(x -> {}, EntityType.SENSOR);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        TaskingCapability taskingCapability = TaskingCapabilityBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            taskingCapability.subscribeRelative(x -> {}, EntityType.SENSOR);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }
    }

    @Test
    public void subscribeEntitySetDirect() throws MalformedURLException, MqttException {
        Assert.assertEquals("v1.0/Actuators", service.actuators().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Datastreams", service.datastreams().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/FeaturesOfInterest", service.featuresOfInterest().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/HistoricalLocations", service.historicalLocations().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/MultiDatastreams", service.multiDatastreams().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Observations", service.observations().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/ObservedProperties", service.observedProperties().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Sensors", service.sensors().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Tasks", service.tasks().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/TaskingCapabilities", service.taskingCapabilities().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Things", service.things().subscribe(x -> {}).getTopic());
    }

    @Test
    public void subscribeEntitySetWithSelect() throws MalformedURLException, MqttException {
        Assert.assertEquals("v1.0/Actuators?$select=" + EntityType.ACTUATOR.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.actuators().subscribe(x -> {}, EntityType.ACTUATOR.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals("v1.0/Datastreams?$select=" + EntityType.DATASTREAM.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.datastreams().subscribe(x -> {}, EntityType.DATASTREAM.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals("v1.0/FeaturesOfInterest?$select=" + EntityType.FEATURE_OF_INTEREST.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.featuresOfInterest().subscribe(x -> {}, EntityType.FEATURE_OF_INTEREST.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals("v1.0/HistoricalLocations?$select=" + EntityType.HISTORICAL_LOCATION.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.historicalLocations().subscribe(x -> {}, EntityType.HISTORICAL_LOCATION.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals("v1.0/MultiDatastreams?$select=" + EntityType.MULTIDATASTREAM.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.multiDatastreams().subscribe(x -> {}, EntityType.MULTIDATASTREAM.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals("v1.0/Observations?$select=" + EntityType.OBSERVATION.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.observations().subscribe(x -> {}, EntityType.OBSERVATION.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals("v1.0/ObservedProperties?$select=" + EntityType.OBSERVED_PROPERTY.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.observedProperties().subscribe(x -> {}, EntityType.OBSERVED_PROPERTY.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals("v1.0/Sensors?$select=" + EntityType.SENSOR.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.sensors().subscribe(x -> {}, EntityType.SENSOR.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals("v1.0/Tasks?$select=" + EntityType.TASK.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.tasks().subscribe(x -> {}, EntityType.TASK.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals("v1.0/TaskingCapabilities?$select=" + EntityType.TASKING_CAPABILITY.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.taskingCapabilities().subscribe(x -> {}, EntityType.TASKING_CAPABILITY.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals("v1.0/Things?$select=" + EntityType.THING.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.things().subscribe(x -> {}, EntityType.THING.getProperties().toArray(new EntityProperty[0])).getTopic());
    }

    @Test
    public void subscribeEntitySetWithSelect_InvalidProperties() throws MalformedURLException, MqttException {
        String errorMessage = "use of unknown property in $select";
        try {
            service.actuators().subscribe(x -> {}, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.datastreams().subscribe(x -> {}, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.featuresOfInterest().subscribe(x -> {}, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.historicalLocations().subscribe(x -> {}, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.multiDatastreams().subscribe(x -> {}, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.observations().subscribe(x -> {}, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.observedProperties().subscribe(x -> {}, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.sensors().subscribe(x -> {}, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.tasks().subscribe(x -> {}, EntityProperty.LOCATION);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.taskingCapabilities().subscribe(x -> {}, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.things().subscribe(x -> {}, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            Assert.assertThat(ex.getMessage(), containsString(errorMessage));
        }
    }

    @Test
    public void subscribeEntitySetRelative() throws MalformedURLException, MqttException {
        Actuator actuator = ActuatorBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/Actuators(1)/TaskingCapabilities", actuator.taskingCapabilities().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Actuators(1)/TaskingCapabilities?$select=id,name", actuator.taskingCapabilities().subscribe(x -> {}, EntityProperty.ID, EntityProperty.NAME).getTopic());

        Datastream datastream = DatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/Datastreams(1)/Observations", datastream.observations().subscribe(x -> {}).getTopic());

        FeatureOfInterest featureOfInterest = FeatureOfInterestBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/FeaturesOfInterest(1)/Observations", featureOfInterest.observations().subscribe(x -> {}).getTopic());

        HistoricalLocation historicalLocation = HistoricalLocationBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/HistoricalLocations(1)/Locations", historicalLocation.locations().subscribe(x -> {}).getTopic());

        MultiDatastream multiDatastream = MultiDatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/MultiDatastreams(1)/Observations", multiDatastream.observations().subscribe(x -> {}).getTopic());

        ObservedProperty observedProperty = ObservedPropertyBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/ObservedProperties(1)/Datastreams", observedProperty.datastreams().subscribe(x -> {}).getTopic());

        Sensor sensor = SensorBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/Sensors(1)/Datastreams", sensor.datastreams().subscribe(x -> {}).getTopic());

        TaskingCapability taskingCapability = TaskingCapabilityBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/TaskingCapabilities(1)/Tasks", taskingCapability.tasks().subscribe(x -> {}).getTopic());

        Thing thing = ThingBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals("v1.0/Things(1)/Datastreams", thing.datastreams().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Things(1)/HistoricalLocations", thing.historicalLocations().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Things(1)/Locations", thing.locations().subscribe(x -> {}).getTopic());
        Assert.assertEquals("v1.0/Things(1)/TaskingCapabilities", thing.taskingCapabilities().subscribe(x -> {}).getTopic());
    }

    private Datastream createDatastreamWithDependencies() throws ServiceFailureException {
        Datastream datastream = DatastreamBuilder.builder()
                .name("datastream name")
                .description("datastream description")
                .unitOfMeasurement(new UnitOfMeasurement("", "", ""))
                .observationType(AbstractDatastreamBuilder.ValueCode.OM_Observation)
                .observedProperty(new ObservedProperty("observedProperty name", "observedProperty definition", "observedProperty description"))
                .sensor(SensorBuilder.builder()
                        .name("sensor name")
                        .description("sensor description")
                        .encodingType(AbstractSensorBuilder.ValueCode.SensorML)
                        .metadata("sensor metadata")
                        .build())
                .thing(ThingBuilder.builder()
                        .name("thing name")
                        .description("thing description")
                        .location(LocationBuilder.builder()
                                .name("location name")
                                .description("location description")
                                .encodingType(AbstractLocationBuilder.ValueCode.GeoJSON)
                                .location(new Point(-114.05, 51.05))
                                .build())
                        .build())
                .build();
        service.create(datastream);
        return datastream;
    }

//    @Test
//    public void createObservation() throws MalformedURLException, MqttException, ServiceFailureException {
//        Datastream datastream = createDatastreamWithDependencies();
//        // v1.0/Observations
//        service.observations().createMqtt(ObservationBuilder.builder()
//                .result("foo")
//                .datastream(datastream)
//                .build());
//        // v1.0/Datastreams([id])/Observations
//        datastream.observations().createMqtt(ObservationBuilder.builder()
//                .result("foo")
//                .build());
//        // v1.0/FeaturesOfInterest([id])/Observations
//        FeatureOfInterest foi = datastream.observations().query().first().getFeatureOfInterest();
//        foi.observations().createMqtt(ObservationBuilder.builder()
//                .result("foo")
//                .datastream(datastream)
//                .build());
//    }

    private TaskingCapability createTaskingCapability() throws ServiceFailureException {
        TaskingCapability taskingCapability = TaskingCapabilityBuilder.builder()
                .name("taskingCapability name")
                .description("taskingCapability description")
                .taskingParameter(TextBuilder.builder()
                        .name("parameterName")
                        .label("parameter label")
                        .description("parameter description")
                        .build())
                .actuator(ActuatorBuilder.builder()
                        .name("actuator name")
                        .description("actuator description")
                        .encodingType(AbstractActuatorBuilder.ValueCode.SensorML)
                        .metadata("actuator metadata")
                        .build())
                .thing(ThingBuilder.builder()
                        .name("thing name")
                        .description("thing description")
                        .build())
                .build();
        service.create(taskingCapability);
        return taskingCapability;
    }

//    @Test
//    public void createTask() throws MalformedURLException, MqttException, ServiceFailureException {
//        TaskingCapability taskingCapability = createTaskingCapability();
//        // v1.0/Tasks
//        service.tasks().createMqtt(TaskBuilder.builder()
//                .taskingParameter("parameterName", "example value")
//                .taskingCapability(taskingCapability)
//                .build());
//        // v1.0/TaskingCapabilities([id])/Tasks
//        taskingCapability.tasks().createMqtt(TaskBuilder.builder().service(service)
//                .taskingParameter("parameterName", "example value")
//                .build());
//    }
}
