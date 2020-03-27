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

import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.TaskingCapability;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.TimeObject;
import de.fraunhofer.iosb.ilt.sta.model.builder.ObservationBuilder;
import de.fraunhofer.iosb.ilt.sta.model.builder.TaskingCapabilityBuilder;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.swe.common.constraint.AllowedValues;
import de.fraunhofer.iosb.ilt.swe.common.simple.Count;
import de.fraunhofer.iosb.ilt.swe.common.simple.Text;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jab
 */
public class EntityReaderTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void readEntity() throws IOException {
        String json = "{\n"
                + "\"phenomenonTime\": \"2016-01-07T02:00:00.000Z\",\n"
                + "\"resultTime\": null,\n"
                + "\"result\": 0.15,\n"
                + "\"Datastream@iot.navigationLink\": \"https://server.de/SensorThingsService/v1.0/Observations(7179373)/Datastream\",\n"
                + "\"FeatureOfInterest@iot.navigationLink\": \"https://server.de/SensorThingsService/v1.0/Observations(7179373)/FeatureOfInterest\",\n"
                + "\"@iot.id\": 7179373,\n"
                + "\"@iot.selfLink\": \"https://server.de/SensorThingsService/v1.0/Observations(7179373)\"\n"
                + "}";
        final ObjectMapper mapper = ObjectMapperFactory.get();
        Observation observation = mapper.readValue(json, Observation.class);

        Observation expected = ObservationBuilder.builder()
                .phenomenonTime(new TimeObject(ZonedDateTime.parse("2016-01-07T02:00:00.000Z")))
                .result(BigDecimal.valueOf(0.15))
                .id(new IdLong(7179373L))
                .build();
        expected.setSelfLink("https://server.de/SensorThingsService/v1.0/Observations(7179373)");

        Assert.assertEquals(expected, observation);
    }

    @Test
    public void readEntityList() throws IOException {
        String json = "{\n"
                + "  \"@iot.nextLink\" : \"https://server.de/SensorThingsService/v1.0/Things?$top=2&$skip=14&$expand=Datastreams%28%24top%3D2%3B%24count%3Dtrue%29\",\n"
                + "  \"value\" : [ {\n"
                + "    \"name\" : \"Recoaro 1000\",\n"
                + "    \"description\" : \"Weather station Recoaro 1000\",\n"
                + "    \"Datastreams@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/Things(19)/Datastreams\",\n"
                + "    \"Datastreams@iot.count\" : 6,\n"
                + "    \"Datastreams\" : [ {\n"
                + "      \"name\" : \"Air Temperature Recoaro 1000\",\n"
                + "      \"description\" : \"The Air Temperature at Recoaro 1000\",\n"
                + "      \"observationType\" : \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n"
                + "      \"phenomenonTime\" : \"2010-01-01T00:00:00.000Z/2019-01-13T06:00:00.000Z\",\n"
                + "      \"unitOfMeasurement\" : {\n"
                + "        \"name\" : \"degree celcius\",\n"
                + "        \"symbol\" : \"°C\",\n"
                + "        \"definition\" : \"ucum:Cel\"\n"
                + "      },\n"
                + "      \"Observations\" : [\n"
                + "        {\"result\": 0.0}\n"
                + "      ],\n"
                + "      \"@iot.id\" : 66,\n"
                + "      \"@iot.selfLink\" : \"https://server.de/SensorThingsService/v1.0/Datastreams(66)\"\n"
                + "    }, {\n"
                + "      \"name\" : \"Precipitation Recoaro 1000\",\n"
                + "      \"description\" : \"The Precipitation at Recoaro 1000\",\n"
                + "      \"observationType\" : \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n"
                + "      \"phenomenonTime\" : \"2010-01-01T00:00:00.000Z/2019-01-13T06:00:00.000Z\",\n"
                + "      \"unitOfMeasurement\" : {\n"
                + "        \"name\" : \"mm/h\",\n"
                + "        \"symbol\" : \"mm/h\",\n"
                + "        \"definition\" : \"ucum:mm/h\"\n"
                + "      },\n"
                + "      \"@iot.id\" : 130,\n"
                + "      \"@iot.selfLink\" : \"https://server.de/SensorThingsService/v1.0/Datastreams(130)\"\n"
                + "    } ],\n"
                + "    \"Datastreams@iot.nextLink\" : \"https://server.de/SensorThingsService/v1.0/Things(19)/Datastreams?$top=2&$skip=2&$count=true\",\n"
                + "    \"MultiDatastreams@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/Things(19)/MultiDatastreams\",\n"
                + "    \"Locations@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/Things(19)/Locations\",\n"
                + "    \"HistoricalLocations@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/Things(19)/HistoricalLocations\",\n"
                + "    \"@iot.id\" : 19,\n"
                + "    \"@iot.selfLink\" : \"https://server.de/SensorThingsService/v1.0/Things(19)\"\n"
                + "  }, {\n"
                + "    \"name\" : \"Valdagno\",\n"
                + "    \"description\" : \"Weather station Valdagno\",\n"
                + "    \"Datastreams@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/Things(20)/Datastreams\",\n"
                + "    \"Datastreams\" : [],\n"
                + "    \"Datastreams@iot.count\" : 6,\n"
                + "    \"Datastreams@iot.nextLink\" : \"https://server.de/SensorThingsService/v1.0/Things(20)/Datastreams?$top=2&$skip=2&$count=true\",\n"
                + "    \"MultiDatastreams@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/Things(20)/MultiDatastreams\",\n"
                + "    \"Locations@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/Things(20)/Locations\",\n"
                + "    \"HistoricalLocations@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/Things(20)/HistoricalLocations\",\n"
                + "    \"@iot.id\" : 20,\n"
                + "    \"@iot.selfLink\" : \"https://server.de/SensorThingsService/v1.0/Things(20)\"\n"
                + "  } ]\n"
                + "}";

        final ObjectMapper mapper = ObjectMapperFactory.get();
        EntityList<Thing> things = mapper.readValue(json, EntityType.THINGS.getTypeReference());
        Assert.assertEquals(URI.create("https://server.de/SensorThingsService/v1.0/Things?$top=2&$skip=14&$expand=Datastreams%28%24top%3D2%3B%24count%3Dtrue%29"), things.getNextLink());
        List<Thing> thingList = things.toList();
        Thing thing = thingList.get(0);
        Assert.assertEquals(new IdLong(19L), thing.getId());
        Assert.assertEquals("Recoaro 1000", thing.getName());
        Assert.assertEquals("Weather station Recoaro 1000", thing.getDescription());

        EntityList<Datastream> datastreams = thing.getDatastreams();
        Assert.assertEquals(URI.create("https://server.de/SensorThingsService/v1.0/Things(19)/Datastreams?$top=2&$skip=2&$count=true"), datastreams.getNextLink());
        Assert.assertEquals(6L, datastreams.getCount());
        List<Datastream> dsList = datastreams.toList();

        Datastream datastream = dsList.get(0);
        Assert.assertEquals(new IdLong(66L), datastream.getId());
        Assert.assertEquals("Air Temperature Recoaro 1000", datastream.getName());
        Assert.assertEquals("The Air Temperature at Recoaro 1000", datastream.getDescription());
        Object result = datastream.getObservations().toList().get(0).getResult();
        Assert.assertEquals(new BigDecimal("0.0"), result);

        datastream = dsList.get(1);
        Assert.assertEquals(new IdLong(130L), datastream.getId());
        Assert.assertEquals("Precipitation Recoaro 1000", datastream.getName());
        Assert.assertEquals("The Precipitation at Recoaro 1000", datastream.getDescription());

        thing = thingList.get(1);
        Assert.assertEquals(new IdLong(20L), thing.getId());
        Assert.assertEquals("Valdagno", thing.getName());
        Assert.assertEquals("Weather station Valdagno", thing.getDescription());

        datastreams = thing.getDatastreams();
        Assert.assertEquals(URI.create("https://server.de/SensorThingsService/v1.0/Things(20)/Datastreams?$top=2&$skip=2&$count=true"), datastreams.getNextLink());
        Assert.assertEquals(6L, datastreams.getCount());

    }

    @Test
    public void readEmptyEntityList() throws IOException {
        String json = "{\n"
                + "  \"value\" : [ ]\n"
                + "}";

        final ObjectMapper mapper = ObjectMapperFactory.get();
        EntityList<Thing> things = mapper.readValue(json, EntityType.THINGS.getTypeReference());
        Assert.assertEquals(null, things.getNextLink());
        List<Thing> thingList = things.toList();

        Assert.assertTrue(thingList.isEmpty());
    }

    @Test
    public void readTaskingCapabilities() throws IOException {
        String json = "{\n"
                + "  \"name\" : \"createNewVA\",\n"
                + "  \"description\" : \"Virtual Actuator Server, starts new Virtual Actuators\",\n"
                + "  \"taskingParameters\" : {\n"
                + "    \"type\" : \"DataRecord\",\n"
                + "    \"field\" : [ {\n"
                + "      \"type\" : \"Text\",\n"
                + "      \"label\" : \"Aktor-Name\",\n"
                + "      \"description\" : \"Name des neuen virtuellen Aktors\",\n"
                + "      \"name\" : \"vaName\"\n"
                + "    }, {\n"
                + "      \"type\" : \"Text\",\n"
                + "      \"label\" : \"Aktor-Beschreibung\",\n"
                + "      \"description\" : \"Beschreibung des neuen virtuellen Aktors\",\n"
                + "      \"name\" : \"vaDescription\"\n"
                + "    } ]\n"
                + "  },\n"
                + "  \"Actuator@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/Actuator\",\n"
                + "  \"Thing@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/TaskingCapabilities(1)/Thing\",\n"
                + "  \"Tasks@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/TaskingCapabilities(1)/Tasks\",\n"
                + "  \"@iot.id\" : 1,\n"
                + "  \"@iot.selfLink\" : \"https://server.de/SensorThingsService/v1.0/TaskingCapabilities(1)\"\n" + "}";
        final ObjectMapper mapper = ObjectMapperFactory.get();
        TaskingCapability observation = mapper.readValue(json, TaskingCapability.class);

        Text vaName = new Text();
        vaName.setLabel("Aktor-Name");
        vaName.setDescription("Name des neuen virtuellen Aktors");

        Text vaDescription = new Text();
        vaDescription.setLabel("Aktor-Beschreibung");
        vaDescription.setDescription("Beschreibung des neuen virtuellen Aktors");

        TaskingCapability expected = TaskingCapabilityBuilder.builder()
                .name("createNewVA")
                .description("Virtual Actuator Server, starts new Virtual Actuators")
                .taskingParameter("vaName", vaName)
                .taskingParameter("vaDescription", vaDescription)
                .id(new IdLong(1L))
                .build();
        expected.setSelfLink("https://server.de/SensorThingsService/v1.0/TaskingCapabilities(1)");

        Assert.assertEquals(expected, observation);
    }

    @Test
    public void readTaskingCapabilitiesWithConstraint() throws IOException {
        String json = "{\n"
                + "  \"name\": \"DatastreamCopierCapability\",\n"
                + "  \"description\": \"Copies Observations from one Datastream to another\",\n"
                + "  \"taskingParameters\": {\n"
                + "    \"type\": \"DataRecord\",\n"
                + "    \"field\": [{\n"
                + "      \"type\": \"Count\",\n"
                + "      \"name\": \"sourceDatastream\",\n"
                + "      \"label\": \"Source Datastream\",\n"
                + "      \"description\": \"ID of the datastream from which the observations should be taken.\",\n"
                + "      \"constraint\": {\n"
                + "        \"type\": \"AllowedValues\",\n"
                + "        \"interval\": [[0, 10000]]\n"
                + "      }\n"
                + "    },\n"
                + "    {\n"
                + "      \"type\": \"Count\",\n"
                + "      \"name\": \"destinationDatastream\",\n"
                + "      \"label\": \"Destination Datastream\",\n"
                + "      \"description\": \"ID of the datastream to which the observations should be copied.\",\n"
                + "      \"constraint\": {\n"
                + "        \"type\": \"AllowedValues\",\n"
                + "        \"interval\": [[0, 10000]]\n"
                + "      }\n"
                + "    }]\n"
                + "  },\n"
                + "  \"Actuator@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/Actuator\",\n"
                + "  \"Thing@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/TaskingCapabilities(1)/Thing\",\n"
                + "  \"Tasks@iot.navigationLink\" : \"https://server.de/SensorThingsService/v1.0/TaskingCapabilities(1)/Tasks\",\n"
                + "  \"@iot.id\" : 1,\n"
                + "  \"@iot.selfLink\" : \"https://server.de/SensorThingsService/v1.0/TaskingCapabilities(1)\"\n"
                + "}";
        final ObjectMapper mapper = ObjectMapperFactory.get();
        TaskingCapability observation = mapper.readValue(json, TaskingCapability.class);

        AllowedValues allowedValues = new AllowedValues();
        List<List<BigDecimal>> interval = new ArrayList<>();
        List<BigDecimal> intervallItem = new ArrayList<>();
        intervallItem.add(new BigDecimal(0));
        intervallItem.add(new BigDecimal(10000));
        interval.add(intervallItem);
        allowedValues.setInterval(interval);

        Count sourceDS = new Count();
        sourceDS.setLabel("Source Datastream");
        sourceDS.setDescription("ID of the datastream from which the observations should be taken.");
        sourceDS.setConstraint(allowedValues);

        Count destDS = new Count();
        destDS.setLabel("Destination Datastream");
        destDS.setDescription("ID of the datastream to which the observations should be copied.");
        destDS.setConstraint(allowedValues);

        TaskingCapability expected = TaskingCapabilityBuilder.builder()
                .name("DatastreamCopierCapability")
                .description("Copies Observations from one Datastream to another")
                .taskingParameter("sourceDatastream", sourceDS)
                .taskingParameter("destinationDatastream", destDS)
                .id(new IdLong(1L))
                .build();
        expected.setSelfLink("https://server.de/SensorThingsService/v1.0/TaskingCapabilities(1)");

        Assert.assertEquals(expected, observation);
    }
}
