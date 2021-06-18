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
import de.fraunhofer.iosb.ilt.sta.model.builder.MultiDatastreamBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.ObservationBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.ObservedPropertyBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.SensorBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.TaskBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.TaskingCapabilityBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.ThingBuilder;
import de.fraunhofer.iosb.ilt.sta.service.MqttConfig;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import static org.hamcrest.CoreMatchers.containsString;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Michael Jacoby
 */
public class MqttTest {

    private SensorThingsService service;
    private String version;

    @Before
    public void setUp() throws MalformedURLException, MqttException, Exception {
        MqttClient mqttClient = mock(MqttClient.class);
        service = new SensorThingsService(new URL("http://localhost:8080/FROST-Server/v1.0"), new MqttConfig("tcp://localhost:1883"));
        Field field = FieldUtils.getField(SensorThingsService.class, "mqttClient", true);
        field.set(service, mqttClient);
        version = service.getVersion().getUrlPattern();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void subscribeEntityDirect() throws MalformedURLException, MqttException {
        Assert.assertEquals(version + "/Actuators(1)", ActuatorBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Datastreams(1)", DatastreamBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/FeaturesOfInterest(1)", FeatureOfInterestBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/HistoricalLocations(1)", HistoricalLocationBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/MultiDatastreams(1)", MultiDatastreamBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Observations(1)", ObservationBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/ObservedProperties(1)", ObservedPropertyBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Sensors(1)", SensorBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Tasks(1)", TaskBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/TaskingCapabilities(1)", TaskingCapabilityBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Things(1)", ThingBuilder.builder().service(service).id(new IdLong(1L)).build().subscribe(x -> {
        }).getTopic());
    }

    @Test
    public void subscribeEntityRelative() throws MalformedURLException, MqttException {
        Datastream datastream = DatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/Datastreams(1)/ObservedProperty", datastream.subscribeRelative(x -> {
        }, EntityType.OBSERVED_PROPERTY).getTopic());
        Assert.assertEquals(version + "/Datastreams(1)/Sensor", datastream.subscribeRelative(x -> {
        }, EntityType.SENSOR).getTopic());
        Assert.assertEquals(version + "/Datastreams(1)/Thing", datastream.subscribeRelative(x -> {
        }, EntityType.THING).getTopic());

        HistoricalLocation historicalLocation = HistoricalLocationBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/HistoricalLocations(1)/Thing", historicalLocation.subscribeRelative(x -> {
        }, EntityType.THING).getTopic());

        MultiDatastream multiDatastream = MultiDatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/MultiDatastreams(1)/Sensor", multiDatastream.subscribeRelative(x -> {
        }, EntityType.SENSOR).getTopic());
        Assert.assertEquals(version + "/MultiDatastreams(1)/Thing", multiDatastream.subscribeRelative(x -> {
        }, EntityType.THING).getTopic());

        Observation observation = ObservationBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/Observations(1)/Datastream", observation.subscribeRelative(x -> {
        }, EntityType.DATASTREAM).getTopic());
        Assert.assertEquals(version + "/Observations(1)/Datastream/ObservedProperty", observation.subscribeRelative(x -> {
        }, EntityType.DATASTREAM, EntityType.OBSERVED_PROPERTY).getTopic());
        Assert.assertEquals(version + "/Observations(1)/Datastream/Sensor", observation.subscribeRelative(x -> {
        }, EntityType.DATASTREAM, EntityType.SENSOR).getTopic());
        Assert.assertEquals(version + "/Observations(1)/Datastream/Thing", observation.subscribeRelative(x -> {
        }, EntityType.DATASTREAM, EntityType.THING).getTopic());
        Assert.assertEquals(version + "/Observations(1)/FeatureOfInterest", observation.subscribeRelative(x -> {
        }, EntityType.FEATURE_OF_INTEREST).getTopic());

        Task task = TaskBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/Tasks(1)/TaskingCapability", task.subscribeRelative(x -> {
        }, EntityType.TASKING_CAPABILITY).getTopic());

        TaskingCapability taskingCapability = TaskingCapabilityBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/TaskingCapabilities(1)/Actuator", taskingCapability.subscribeRelative(x -> {
        }, EntityType.ACTUATOR).getTopic());
        Assert.assertEquals(version + "/TaskingCapabilities(1)/Thing", taskingCapability.subscribeRelative(x -> {
        }, EntityType.THING).getTopic());
    }

    @Test
    public void subscribeEntityRelative_Invalid() throws MalformedURLException, MqttException {
        String errorMessage = "invalid relative subscription path";

        Datastream datastream = DatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            datastream.subscribeRelative(x -> {
            }, EntityType.DATASTREAM);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        HistoricalLocation historicalLocation = HistoricalLocationBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            historicalLocation.subscribeRelative(x -> {
            }, EntityType.HISTORICAL_LOCATION);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        MultiDatastream multiDatastream = MultiDatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            multiDatastream.subscribeRelative(x -> {
            }, EntityType.DATASTREAM);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        Observation observation = ObservationBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            observation.subscribeRelative(x -> {
            }, EntityType.THING);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        Task task = TaskBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            task.subscribeRelative(x -> {
            }, EntityType.SENSOR);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        TaskingCapability taskingCapability = TaskingCapabilityBuilder.builder().service(service).id(new IdLong(1L)).build();
        try {
            taskingCapability.subscribeRelative(x -> {
            }, EntityType.SENSOR);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }
    }

    @Test
    public void subscribeEntitySetDirect() throws MalformedURLException, MqttException {
        Assert.assertEquals(version + "/Actuators", service.actuators().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Datastreams", service.datastreams().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/FeaturesOfInterest", service.featuresOfInterest().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/HistoricalLocations", service.historicalLocations().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/MultiDatastreams", service.multiDatastreams().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Observations", service.observations().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/ObservedProperties", service.observedProperties().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Sensors", service.sensors().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Tasks", service.tasks().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/TaskingCapabilities", service.taskingCapabilities().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Things", service.things().subscribe(x -> {
        }).getTopic());
    }

    @Test
    public void subscribeEntitySetWithSelect() throws MalformedURLException, MqttException {
        Assert.assertEquals(version + "/Actuators?$select=" + EntityType.ACTUATOR.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.actuators().subscribe(x -> {
                }, EntityType.ACTUATOR.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals(version + "/Datastreams?$select=" + EntityType.DATASTREAM.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.datastreams().subscribe(x -> {
                }, EntityType.DATASTREAM.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals(version + "/FeaturesOfInterest?$select=" + EntityType.FEATURE_OF_INTEREST.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.featuresOfInterest().subscribe(x -> {
                }, EntityType.FEATURE_OF_INTEREST.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals(version + "/HistoricalLocations?$select=" + EntityType.HISTORICAL_LOCATION.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.historicalLocations().subscribe(x -> {
                }, EntityType.HISTORICAL_LOCATION.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals(version + "/MultiDatastreams?$select=" + EntityType.MULTIDATASTREAM.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.multiDatastreams().subscribe(x -> {
                }, EntityType.MULTIDATASTREAM.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals(version + "/Observations?$select=" + EntityType.OBSERVATION.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.observations().subscribe(x -> {
                }, EntityType.OBSERVATION.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals(version + "/ObservedProperties?$select=" + EntityType.OBSERVED_PROPERTY.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.observedProperties().subscribe(x -> {
                }, EntityType.OBSERVED_PROPERTY.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals(version + "/Sensors?$select=" + EntityType.SENSOR.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.sensors().subscribe(x -> {
                }, EntityType.SENSOR.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals(version + "/Tasks?$select=" + EntityType.TASK.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.tasks().subscribe(x -> {
                }, EntityType.TASK.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals(version + "/TaskingCapabilities?$select=" + EntityType.TASKING_CAPABILITY.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.taskingCapabilities().subscribe(x -> {
                }, EntityType.TASKING_CAPABILITY.getProperties().toArray(new EntityProperty[0])).getTopic());

        Assert.assertEquals(version + "/Things?$select=" + EntityType.THING.getProperties().stream().map(x -> x.getName()).collect(Collectors.joining(",")),
                service.things().subscribe(x -> {
                }, EntityType.THING.getProperties().toArray(new EntityProperty[0])).getTopic());
    }

    @Test
    public void subscribeEntitySetWithSelect_InvalidProperties() throws MalformedURLException, MqttException {
        String errorMessage = "use of unknown property in $select";
        try {
            service.actuators().subscribe(x -> {
            }, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.datastreams().subscribe(x -> {
            }, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.featuresOfInterest().subscribe(x -> {
            }, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.historicalLocations().subscribe(x -> {
            }, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.multiDatastreams().subscribe(x -> {
            }, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.observations().subscribe(x -> {
            }, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.observedProperties().subscribe(x -> {
            }, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.sensors().subscribe(x -> {
            }, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.tasks().subscribe(x -> {
            }, EntityProperty.LOCATION);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.taskingCapabilities().subscribe(x -> {
            }, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }

        try {
            service.things().subscribe(x -> {
            }, EntityProperty.CREATIONTIME);
            Assert.fail("expected MqttException to be thrown");
        } catch (MqttException ex) {
            MatcherAssert.assertThat(ex.getMessage(), containsString(errorMessage));
        }
    }

    @Test
    public void subscribeEntitySetRelative() throws MalformedURLException, MqttException {
        Actuator actuator = ActuatorBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/Actuators(1)/TaskingCapabilities", actuator.taskingCapabilities().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Actuators(1)/TaskingCapabilities?$select=id,name", actuator.taskingCapabilities().subscribe(x -> {
        }, EntityProperty.ID, EntityProperty.NAME).getTopic());

        Datastream datastream = DatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/Datastreams(1)/Observations", datastream.observations().subscribe(x -> {
        }).getTopic());

        FeatureOfInterest featureOfInterest = FeatureOfInterestBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/FeaturesOfInterest(1)/Observations", featureOfInterest.observations().subscribe(x -> {
        }).getTopic());

        HistoricalLocation historicalLocation = HistoricalLocationBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/HistoricalLocations(1)/Locations", historicalLocation.locations().subscribe(x -> {
        }).getTopic());

        MultiDatastream multiDatastream = MultiDatastreamBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/MultiDatastreams(1)/Observations", multiDatastream.observations().subscribe(x -> {
        }).getTopic());

        ObservedProperty observedProperty = ObservedPropertyBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/ObservedProperties(1)/Datastreams", observedProperty.datastreams().subscribe(x -> {
        }).getTopic());

        Sensor sensor = SensorBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/Sensors(1)/Datastreams", sensor.datastreams().subscribe(x -> {
        }).getTopic());

        TaskingCapability taskingCapability = TaskingCapabilityBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/TaskingCapabilities(1)/Tasks", taskingCapability.tasks().subscribe(x -> {
        }).getTopic());

        Thing thing = ThingBuilder.builder().service(service).id(new IdLong(1L)).build();
        Assert.assertEquals(version + "/Things(1)/Datastreams", thing.datastreams().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Things(1)/HistoricalLocations", thing.historicalLocations().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Things(1)/Locations", thing.locations().subscribe(x -> {
        }).getTopic());
        Assert.assertEquals(version + "/Things(1)/TaskingCapabilities", thing.taskingCapabilities().subscribe(x -> {
        }).getTopic());
    }
}
