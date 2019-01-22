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

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.TimeObject;
import de.fraunhofer.iosb.ilt.sta.model.builder.ObservationBuilder;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
                + "\"Datastream@iot.navigationLink\": \"https://beaware.server.de/SensorThingsService/v1.0/Observations(7179373)/Datastream\",\n"
                + "\"FeatureOfInterest@iot.navigationLink\": \"https://beaware.server.de/SensorThingsService/v1.0/Observations(7179373)/FeatureOfInterest\",\n"
                + "\"@iot.id\": 7179373,\n"
                + "\"@iot.selfLink\": \"https://beaware.server.de/SensorThingsService/v1.0/Observations(7179373)\"\n"
                + "}";
        final ObjectMapper mapper = ObjectMapperFactory.get();
        Observation observation = mapper.readValue(json, Observation.class);

        Observation expected = ObservationBuilder.builder()
                .phenomenonTime(new TimeObject(ZonedDateTime.parse("2016-01-07T02:00:00.000Z")))
                .result(BigDecimal.valueOf(0.15))
                .id(new IdLong(7179373L))
                .build();
        expected.setSelfLink("https://beaware.server.de/SensorThingsService/v1.0/Observations(7179373)");

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
}
