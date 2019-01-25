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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.IdString;
import de.fraunhofer.iosb.ilt.sta.model.Location;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
import de.fraunhofer.iosb.ilt.sta.model.Sensor;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geojson.Point;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.threeten.extra.Interval;

/**
 *
 * @author jab
 */
public class EntityFormatterTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void writeThing_Basic_Success() throws IOException {
        String expResult
                = "{\n"
                + "\"@iot.id\": 1,\n"
                + "\"name\": \"This thing is an oven.\",\n"
                + "\"description\": \"This thing is an oven.\",\n"
                + "\"properties\": {\n"
                + "\"owner\": \"John Doe\",\n"
                + "\"color\": \"Silver\"\n"
                + "}\n"
                + "}";
        Thing entity = new Thing();
        entity.setId(new IdLong(1L));
        entity.setName("This thing is an oven.");
        entity.setDescription("This thing is an oven.");
        Map<String, Object> properties = new HashMap<>();
        properties.put("owner", "John Doe");
        properties.put("color", "Silver");
        entity.setProperties(properties);

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);
        assert (jsonEqual(expResult, json));
    }

    @Test
    public void writeThing_Basic_StringId_Success() throws IOException {
        String expResult
                = "{\n"
                + "\"@iot.id\": \"aStringAsId\",\n"
                + "\"name\": \"This thing is an oven.\",\n"
                + "\"description\": \"This thing is an oven.\",\n"
                + "\"properties\": {\n"
                + "\"owner\": \"John Doe\",\n"
                + "\"color\": \"Silver\"\n"
                + "}\n"
                + "}";
        Thing entity = new Thing();
        entity.setId(new IdString("aStringAsId"));
        entity.setName("This thing is an oven.");
        entity.setDescription("This thing is an oven.");
        Map<String, Object> properties = new HashMap<>();
        properties.put("owner", "John Doe");
        properties.put("color", "Silver");
        entity.setProperties(properties);

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);
        assert (jsonEqual(expResult, json));
    }

    @Test
    public void writeThing_CompletelyEmpty_Success() throws IOException {
        String expResult
                = "{}";
        Thing entity = new Thing();
        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);
        assert (jsonEqual(expResult, json));
    }

    @Test
    public void writeThingWithLocation() throws IOException {
        String expResult
                = "{\n"
                + "\"@iot.id\": 1,\n"
                + "\"name\": \"This thing is an oven.\",\n"
                + "\"description\": \"This thing is an oven.\",\n"
                + "\"properties\": {},\n"
                + "\"Locations\": ["
                + "  {"
                + "    \"@iot.id\": 1\n"
                + "  }"
                + "]"
                + "}";
        Thing entity = new Thing();
        entity.setId(new IdLong(1L));
        entity.setName("This thing is an oven.");
        entity.setDescription("This thing is an oven.");
        Map<String, Object> properties = new HashMap<>();
        entity.setProperties(properties);
        Location location = new Location();
        location.setId(new IdLong(1L));
        entity.getLocations().add(location);

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);
        assert (jsonEqual(expResult, json));
    }

    @Test
    public void testIncorrectCollection() throws IOException {
        String expResult = "{\n"
                + "	\"name\": \"TestThing\",\n"
                + "	\"description\": \"A Thing for testing.\",\n"
                + "	\"Locations\": [\n"
                + "		{\n"
                + "			\"name\": \"TestLocation\",\n"
                + "			\"description\": \"The location of the TestThing\",\n"
                + "			\"encodingType\": \"application/vnd.geo+json\",\n"
                + "			\"location\": {\n"
                + "				\"type\": \"Point\",\n"
                + "				\"coordinates\": [8.8, 49.9]\n"
                + "			}\n"
                + "		}\n"
                + "	]\n"
                + "}\n"
                + "";

        Thing entity = new Thing("TestThing", "A Thing for testing.");
        Location location = new Location("TestLocation", "The location of the TestThing", "application/vnd.geo+json", new Point(8.8, 49.9));
        List<Location> locations = new ArrayList<>();
        locations.add(location);
        entity.setLocations(locations);
        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);
        assert (jsonEqual(expResult, json));
    }

    @Test
    public void writeLocation_GeoJson() throws Exception {
        String expResult
                = "{\n"
                + "	\"@iot.id\": 1,\n"
                + " \"name\": \"OvenLocation\",\n"
                + " \"description\": \"The location of an oven.\",\n"
                + "	\"encodingType\": \"application/vnd.geo+json\","
                + "  \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }\n"
                + "}";
        Location entity = new Location();
        entity.setId(new IdLong(1L));
        entity.setName("OvenLocation");
        entity.setDescription("The location of an oven.");
        entity.setEncodingType("application/vnd.geo+json");
        entity.setLocation(new Point(-114.05, 51.05));
        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);
        assert (jsonEqual(expResult, json));

        Location parsed = mapper.readValue(expResult, Location.class);
        Assert.assertEquals(entity, parsed);
    }

    @Test
    public void writeLocation_String() throws Exception {
        String expResult
                = "{\n"
                + "	\"@iot.id\": 1,\n"
                + " \"name\": \"OvenLocation\",\n"
                + " \"description\": \"The location of an oven.\",\n"
                + "	\"encodingType\": \"text/plain\","
                + "  \"location\": \"Third house on the left.\"\n"
                + "}";
        Location entity = new Location();
        entity.setId(new IdLong(1L));
        entity.setName("OvenLocation");
        entity.setDescription("The location of an oven.");
        entity.setEncodingType("text/plain");
        entity.setLocation("Third house on the left.");
        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);

        assert (jsonEqual(expResult, json));

        Location parsed = mapper.readValue(expResult, Location.class);
        Assert.assertEquals(entity, parsed);
    }

    @Test
    public void writeEverything() throws Exception {
        String expResult
                = "{\n"
                + "    \"description\": \"thing 1\",\n"
                + "    \"name\": \"thing name 1\",\n"
                + "    \"properties\": {\n"
                + "        \"reference\": \"first\"\n"
                + "    },\n"
                + "    \"Locations\": [\n"
                + "        {\n"
                + "            \"description\": \"location 1\",\n"
                + "            \"name\": \"location name 1\",\n"
                + "            \"location\": {\n"
                + "                \"type\": \"Point\",\n"
                + "                \"coordinates\": [-117.05, 51.05]\n"
                + "            },\n"
                + "            \"encodingType\": \"application/vnd.geo+json\"\n"
                + "        }\n"
                + "    ],\n"
                + "    \"Datastreams\": [\n"
                + "        {\n"
                + "            \"unitOfMeasurement\": {\n"
                + "                \"name\": \"Lumen\",\n"
                + "                \"symbol\": \"lm\",\n"
                + "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html/Lumen\"\n"
                + "            },\n"
                + "            \"description\": \"datastream 1\",\n"
                + "            \"name\": \"datastream name 1\",\n"
                + "            \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n"
                + "            \"ObservedProperty\": {\n"
                + "                \"name\": \"Luminous Flux\",\n"
                + "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html/LuminousFlux\",\n"
                + "                \"description\": \"observedProperty 1\"\n"
                + "            },\n"
                + "            \"Sensor\": {\n"
                + "                \"description\": \"sensor 1\",\n"
                + "                \"name\": \"sensor name 1\",\n"
                + "                \"encodingType\": \"application/pdf\",\n"
                + "                \"metadata\": \"Light flux sensor\"\n"
                + "            },\n"
                + "            \"Observations\": [\n"
                + "                {\n"
                + "                    \"phenomenonTime\": \"2015-03-03T00:00:00Z\",\n"
                + "                    \"result\": 3\n"
                + "                },\n"
                + "                {\n"
                + "                    \"phenomenonTime\": \"2015-03-04T00:00:00Z\",\n"
                + "                    \"result\": 4\n"
                + "                }\n"
                + "            ]\n"
                + "        },\n"
                + "        {\n"
                + "            \"unitOfMeasurement\": {\n"
                + "                \"name\": \"Centigrade\",\n"
                + "                \"symbol\": \"C\",\n"
                + "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html/Lumen\"\n"
                + "            },\n"
                + "            \"description\": \"datastream 2\",\n"
                + "            \"name\": \"datastream name 2\",\n"
                + "            \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n"
                + "            \"ObservedProperty\": {\n"
                + "                \"name\": \"Tempretaure\",\n"
                + "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html/Tempreture\",\n"
                + "                \"description\": \"observedProperty 2\"\n"
                + "            },\n"
                + "            \"Sensor\": {\n"
                + "                \"description\": \"sensor 2\",\n"
                + "                \"name\": \"sensor name 2\",\n"
                + "                \"encodingType\": \"application/pdf\",\n"
                + "                \"metadata\": \"Tempreture sensor\"\n"
                + "            },\n"
                + "            \"Observations\": [\n"
                + "                {\n"
                + "                    \"phenomenonTime\": \"2015-03-05T00:00:00Z\",\n"
                + "                    \"result\": 5\n"
                + "                },\n"
                + "                {\n"
                + "                    \"phenomenonTime\": \"2015-03-06T00:00:00Z\",\n"
                + "                    \"result\": 6\n"
                + "                }\n"
                + "            ]\n"
                + "        }\n"
                + "    ]\n"
                + "}";
        Thing thing = new Thing();
        thing.setName("thing name 1");
        thing.setDescription("thing 1");
        Map<String, Object> properties = new HashMap<>();
        properties.put("reference", "first");
        thing.setProperties(properties);

        Location location = new Location();
        location.setName("location name 1");
        location.setDescription("location 1");
        location.setLocation(new Point(-117.05, 51.05));
        location.setEncodingType("application/vnd.geo+json");
        thing.getLocations().add(location);

        UnitOfMeasurement um1 = new UnitOfMeasurement("Lumen", "lm", "http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html/Lumen");
        Datastream ds1 = new Datastream("datastream name 1", "datastream 1", "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement", um1);
        ds1.setObservedProperty(new ObservedProperty("Luminous Flux", new URI("http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html/LuminousFlux"), "observedProperty 1"));
        ds1.setSensor(new Sensor("sensor name 1", "sensor 1", "application/pdf", "Light flux sensor"));
        ds1.getObservations().add(new Observation(3, ZonedDateTime.parse("2015-03-03T00:00:00Z")));
        ds1.getObservations().add(new Observation(4, ZonedDateTime.parse("2015-03-04T00:00:00Z")));
        thing.getDatastreams().add(ds1);

        UnitOfMeasurement um2 = new UnitOfMeasurement("Centigrade", "C", "http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html/Lumen");
        Datastream ds2 = new Datastream("datastream name 2", "datastream 2", "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement", um2);
        ds2.setObservedProperty(new ObservedProperty("Tempretaure", new URI("http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html/Tempreture"), "observedProperty 2"));
        ds2.setSensor(new Sensor("sensor name 2", "sensor 2", "application/pdf", "Tempreture sensor"));
        ds2.getObservations().add(new Observation(5, ZonedDateTime.parse("2015-03-05T00:00:00Z")));
        ds2.getObservations().add(new Observation(6, ZonedDateTime.parse("2015-03-06T00:00:00Z")));
        thing.getDatastreams().add(ds2);

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(thing);
        assert (jsonEqual(expResult, json));

        Thing parsed = mapper.readValue(expResult, Thing.class);
        Assert.assertEquals(thing, parsed);
    }

    @Test
    public void writeObservationDateTime() throws IOException {
        String expResult
                = "{\n"
                + "	\"@iot.id\": 1,\n"
                + "	\"phenomenonTime\": \"2014-12-31T11:59:59Z\",\n"
                + "	\"result\": 70.40\n"
                + "}";
        Observation entity = new Observation();
        entity.setId(new IdLong(1L));
        entity.setResult(new BigDecimal("70.40"));
        entity.setPhenomenonTimeFrom(ZonedDateTime.parse("2014-12-31T11:59:59Z"));

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);
        assert (jsonEqual(expResult, json));

        Observation parsed = mapper.readValue(expResult, Observation.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));
    }

    @Test
    public void writeObservationInterval() throws IOException {
        String expResult
                = "{\n"
                + "	\"@iot.id\": 1,\n"
                + "	\"phenomenonTime\": \"2014-12-31T11:59:59Z/2014-12-31T12:01:01Z\",\n"
                + "	\"result\": 70.40\n"
                + "}";
        Observation entity = new Observation();
        entity.setId(new IdLong(1L));
        entity.setResult(new BigDecimal("70.40"));
        entity.setPhenomenonTimeFrom(Interval.parse("2014-12-31T11:59:59Z/2014-12-31T12:01:01Z"));

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);
        assert (jsonEqual(expResult, json));

        Observation parsed = mapper.readValue(expResult, Observation.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));
    }

    @Test
    public void writeObservationNull() throws IOException {
        String expResult
                = "{\n"
                + "	\"@iot.id\": 1,\n"
                + "	\"phenomenonTime\": \"2014-12-31T11:59:59Z\",\n"
                + "	\"result\": null\n"
                + "}";
        Observation entity = new Observation();
        entity.setId(new IdLong(1L));
        entity.setResult(null);
        entity.setPhenomenonTimeFrom(ZonedDateTime.parse("2014-12-31T11:59:59Z"));

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);
        assert (jsonEqual(expResult, json));

        Observation parsed = mapper.readValue(expResult, Observation.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));
    }

    @Test
    public void writeObservationZero() throws IOException {
        String expResult
                = "{\n"
                + "	\"result\": 0.0\n"
                + "}";
        Observation entity = new Observation();
        entity.setResult(new BigDecimal("0.0"));

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(entity);
        assert (jsonEqual(expResult, json));

        Observation parsed = mapper.readValue(expResult, Observation.class);
        Assert.assertEquals(entity.getResult(), parsed.getResult());

        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeObservationNoResult() throws IOException {
        Observation entity = new Observation();
        entity.setId(new IdLong(1L));
        entity.setPhenomenonTimeFrom(ZonedDateTime.parse("2014-12-31T11:59:59Z"));
        SensorThingsService service = new SensorThingsService(new URL("http://localhost:8080/v1.0"));
        try {
            service.create(entity);
        } catch (ServiceFailureException ex) {
            Assert.fail("Expected IllegalArgumentException, not ServiceFailureException");
        }
        Assert.fail("Expeted IllegalArgumentException, got none");
    }

    private boolean jsonEqual(String string1, String string2) {
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        try {
            JsonNode json1 = mapper.readTree(string1);
            JsonNode json2 = mapper.readTree(string2);
            return json1.equals(json2);
        } catch (IOException ex) {
            Logger.getLogger(EntityFormatterTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
