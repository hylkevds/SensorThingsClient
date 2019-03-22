package de.iosb.fraunhofer.ilt.sta;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.*;
import de.fraunhofer.iosb.ilt.sta.model.builder.*;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import org.geojson.GeoJsonObject;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityBuilderTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void buildActuator() throws IOException {
        String expResult = "{\n" +
                "\"name\":\"defaultActuator\",\n"
                + "\"description\":\"defaultDescription\",\n"
                + "\"encodingType\":\"default\",\n"
                + "\"metadata\":\"defaultMetadata\",\n"
                + "\"@iot.id\":1\n"
                + "}";

        ActuatorBuilder builder = new ActuatorBuilder();
        builder.aDefaultActuator();
        Actuator newActuator = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newActuator);
        assert (jsonEqual(expResult, json));

        Actuator parsed = mapper.readValue(expResult, Actuator.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));
    }

    @Test
    public void buildNotDefaultActuator() throws IOException {
        String expResult = "{\n"
                + "\"name\":\"testActuator\",\n"
                + "\"description\":\"Test description\",\n"
                + "\"encodingType\":\"String\",\n"
                + "\"metadata\":\"test\",\n"
                + "\"@iot.id\":1\n"
                + "}";

        ActuatorBuilder builder = new ActuatorBuilder();
        builder.withDescription("Test description");
        builder.withId(1l);
        builder.withName("testActuator");
        builder.withEncodingType("String");
        builder.withMetadata("test");
        EntityList<TaskingCapability> capabilities = new EntityList<TaskingCapability>(EntityType.TASKING_CAPABILITY);
        builder.withTaskingCapability(capabilities);
        Actuator newActuator = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newActuator);
        assert (jsonEqual(expResult, json));

        Actuator parsed = mapper.readValue(expResult, Actuator.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));
    }

    @Test
    public void buildDatastream() throws IOException {
        String expResult = "{\n"
                + "\"name\":\"defaultDatastream\",\n"
                + "\"description\":\"defaultDescription\",\n"
                + "\"observationType\":\"defaultObservationType\",\n"
                + "\"unitOfMeasurement\":{\n"
                + "\"name\":\"defaultUnit\",\n"
                + "\"symbol\":\"defaultSym\",\n"
                + "\"definition\":\"defaultDef\"},\n"
                + "\"@iot.id\":1,\n"
                + "\"Thing\":{\n"
                + "\"@iot.id\":1\n"
                + "},\n"
                + "\"Sensor\":{\n"
                + "\"@iot.id\":1\n"
                + "},\n"
                + "\"ObservedProperty\":{\n"
                + "\"@iot.id\":1\n"
                + "}\n"
                + "}";

        DatastreamBuilder builder = DatastreamBuilder.builder();
        builder.aDefaultDatastream();
        Datastream newDatastream = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newDatastream);
        assert (jsonEqual(expResult, json));

        Datastream parsed = mapper.readValue(expResult, Datastream.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));
    }

    @Test
    public void buildDefaultDatastream() throws
            IOException {
        DatastreamBuilder builder = DatastreamBuilder.builder();
        builder.withName("defaultDatastream");
        builder.withDescription("defaultDescription");
        builder.withId(1L);
        UnitOfMeasurement unitOFMeasurement = new UnitOfMeasurement("defaultUnit", "defaultSym", "defaultDef");
        builder.withUnitOfMeasurement(unitOFMeasurement);
        builder.withObservationType("defaultObservationType");
        ObservedProperty observedProperty = new ObservedProperty();
        observedProperty.setId(new IdLong(1l));
        builder.withObservedProperty(observedProperty.withOnlyId());
        Sensor sensor = new Sensor();
        sensor.setId(new IdLong(1l));
        builder.withSensor(sensor.withOnlyId());
        Thing thing = new Thing();
        thing.setId(new IdLong(1l));
        builder.withThing(thing);
        Datastream newDatastream = builder.build();
        builder.aDefaultDatastream();
        Datastream defaultDatastream = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newDatastream);
        String jsonbuilded = mapper.writeValueAsString(defaultDatastream);
        assert (jsonEqual(jsonbuilded, json));

        Datastream parsed = mapper.readValue(jsonbuilded, Datastream.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(jsonbuilded, json2));
    }

    @Test
    public void buildFeatureOfInterest() throws IOException {
        FeatureOfInterest featureOfInterest = new FeatureOfInterest();
        FeatureOfInterestBuilder builder = FeatureOfInterestBuilder.builder();
        FeatureOfInterest buildedFeature = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(featureOfInterest);
        String jsonbuilded = mapper.writeValueAsString(buildedFeature);
        assert (jsonEqual(jsonbuilded, json));

        FeatureOfInterest parsed = mapper.readValue(jsonbuilded, FeatureOfInterest.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(jsonbuilded, json2));
    }

    @Test
    public void buildHistoricalLocation() throws IOException {
        HistoricalLocation historicalLocation = new HistoricalLocation();
        HistoricalLocationBuilder builder = HistoricalLocationBuilder.builder();
        HistoricalLocation buildedLocation = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(historicalLocation);
        String jsonbuilded = mapper.writeValueAsString(buildedLocation);
        assert (jsonEqual(jsonbuilded, json));

        HistoricalLocation parsed = mapper.readValue(jsonbuilded, HistoricalLocation.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(jsonbuilded, json2));
    }

    @Test
    public void buildLocation() throws IOException {
        LocationBuilder builder = LocationBuilder.builder();
        builder.withDescription("defaultLocation");
        builder.withName("defaultName");
        builder.withEncodingType("application/vnd.geo+json");
        String locString = "{\n" +
                "       \"type\": \"Point\",\n" +
                "       \"coordinates\": [123.4, 0.00]" +
                "}";
        ObjectMapper locationMapper = new ObjectMapper();
        GeoJsonObject geolocation = locationMapper.readValue(locString, GeoJsonObject.class);
        builder.withLocation(geolocation);
        builder.withId(1l);
        Location newLocation = builder.build();
        builder.aDefaultLocation();
        Location defaultLocation = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newLocation);
        String jsonbuilded = mapper.writeValueAsString(defaultLocation);
        assert (jsonEqual(jsonbuilded, json));

        Location parsed = mapper.readValue(jsonbuilded, Location.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(jsonbuilded, json2));
        assert (defaultLocation.equals(newLocation));
        assert (defaultLocation != newLocation);
    }

    @Test
    public void buildMultiDatastream() throws IOException {
        MultiDatastream multiDatastream = new MultiDatastream();
        MultiDatastreamBuilder builder = MultiDatastreamBuilder.builder();
        MultiDatastream buildedDatastream = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(multiDatastream);
        String jsonbuilded = mapper.writeValueAsString(buildedDatastream);
        assert (jsonEqual(jsonbuilded, json));

        MultiDatastream parsed = mapper.readValue(jsonbuilded, MultiDatastream.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(jsonbuilded, json2));
    }

    @Test
    public void buildObservation() throws IOException {
        Observation observation = new Observation();
        ObservationBuilder builder = ObservationBuilder.builder();
        Observation buildedObservation = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(observation);
        String jsonbuilded = mapper.writeValueAsString(buildedObservation);
        assert (jsonEqual(jsonbuilded, json));

        Observation parsed = mapper.readValue(jsonbuilded, Observation.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(jsonbuilded, json2));
    }

    @Test
    public void buildObservedProperty() throws IOException {
        String expResult = "{\n"
                + "\"name\":\"defaultObservedProperty\",\n"
                + "\"definition\":\"default.uri\",\n"
                + "\"description\":\"defaultDescription\",\n"
                + "\"@iot.id\":1\n"
                + "}";

        ObservedPropertyBuilder builder = ObservedPropertyBuilder.builder();
        builder.aDefaultObservedProperty();
        ObservedProperty newObservedProperty = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newObservedProperty);
        assert (jsonEqual(expResult, json));

        ObservedProperty parsed = mapper.readValue(expResult, ObservedProperty.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));

        ObservedPropertyBuilder builder2 = ObservedPropertyBuilder.builder();
        builder2.withName("defaultObservedProperty");
        builder2.withDescription("defaultDescription");
        builder2.withId(1L);
        builder2.withDefinition("default.uri");
        ObservedProperty secondProperty = builder2.build();
        assert (secondProperty.equals(newObservedProperty));
        assert (secondProperty != newObservedProperty);
    }

    @Test
    public void buildSensor() throws IOException {
        String expResult = "{\n"
                + "\"name\":\"defaultSensor\",\n"
                + "\"description\":\"defaultDescription\",\n"
                + "\"encodingType\":\"default\",\n"
                + "\"metadata\":\"defaultMetadata\",\n"
                + "\"@iot.id\":1\n"
                + "}";
        SensorBuilder builder = SensorBuilder.builder();
        builder.aDefaultSensor();
        Sensor newSensor = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newSensor);
        assert (jsonEqual(expResult, json));

        Sensor parsed = mapper.readValue(expResult, Sensor.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));

        SensorBuilder builder2 = SensorBuilder.builder();
        builder2.withName("defaultSensor");
        builder2.withDescription("defaultDescription");
        builder2.withEncodingType("default");
        builder2.withId(1L);
        builder2.withMetadata("defaultMetadata");
        Sensor secondSensor = builder2.build();

        assert (newSensor.equals(secondSensor));
        assert (newSensor != secondSensor);
    }

    @Test
    public void buildTask() throws IOException {

        TaskBuilder builder = new TaskBuilder();
        builder.aDefaultTask();
        Task newTask = builder.build();

        String expResult = "{\n"
                + "\"taskingParameters\": {},\n"
                + "\"@iot.id\":1}";

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newTask);
        assert (jsonEqual(expResult, json));

        Task parsed = mapper.readValue(expResult, Task.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));

        TaskBuilder builder2 = new TaskBuilder();
        builder2.withId(1l);
        Task secondTask = builder2.build();
        assert (secondTask.equals(newTask));
        assert (secondTask != newTask);
        Map<String, Object> taskingParameters = new HashMap<>();
        taskingParameters.put("state", "blau");
        builder2.withParameters(taskingParameters);
        secondTask = builder2.build();
        assert (!secondTask.equals(newTask));
    }

    @Test
    public void buildTaskingCapability() throws IOException {
        String expResult = "{\n"
                + "\"name\":\"defaultTaskingCapability\",\n"
                + "\"description\":\"default\",\n"
                + "\"taskingParameters\":{},\n"
                + "\"@iot.id\":1\n"
                + "}";

        TaskingCapabilityBuilder builder = new TaskingCapabilityBuilder();
        builder.aDefaultTaskingCapability();
        TaskingCapability newCapability = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newCapability);
        assert (jsonEqual(expResult, json));

        TaskingCapability parsed = mapper.readValue(expResult, TaskingCapability.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));

        TaskingCapabilityBuilder builder2 = new TaskingCapabilityBuilder();
        builder2.withDescription("default");
        builder2.withName("defaultTaskingCapability");
        builder2.withId(1l);
        TaskingCapability secondCapability = builder2.build();

        String json3 = mapper.writeValueAsString(secondCapability);
        assert (jsonEqual(json3, json));

        TaskingCapability parsed2 = mapper.readValue(json, TaskingCapability.class);
        String json4 = mapper.writeValueAsString(parsed2);
        assert (jsonEqual(json, json4));
        assert (newCapability != secondCapability);
        assert (newCapability.equals(secondCapability));
    }

    @Test
    public void buildThing() throws IOException {
        String expResult = "{\n" +
                "\"name\":\"defaultThing\",\n"
                + "\"description\":\"defaultDescription\",\n"
                + "\"properties\":{\n"
                + "\"defaulProperty\":\"defaultValue\"\n"
                + "},\n"
                + "\"@iot.id\":1,\n"
                + "\"Locations\":[\n"
                + "{\n"
                + "\"@iot.id\":1\n"
                + "}\n"
                + "]\n"
                + "}";

        ThingBuilder builder = new ThingBuilder();
        builder.aDefaultThing();
        Thing newThing = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newThing);
        assert (jsonEqual(expResult, json));

        Thing parsed = mapper.readValue(expResult, Thing.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));
    }

    @Test
    public void buildNotDefaultThing() throws IOException {
        String expResult = "{\n"
                + "\"name\":\"test name\",\n"
                + "\"description\":\"test description\",\n"
                + "\"properties\":{\n"
                + "},\n"
                + "\"@iot.id\":1,\n"
                + "\"Locations\":[\n"
                + "{\n"
                + "}\n"
                + "]"
                + "}";

        ThingBuilder builder = ThingBuilder.builder();
        builder.withDescription("test description");
        builder.withId(1l);
        builder.withName("test name");
        Map<String, Object> properties = new HashMap<>();
        builder.withProperies(properties);
        Location newLocation = new Location();
        builder.withLocation(newLocation);

        Thing newThing = builder.build();

        final ObjectMapper mapper = ObjectMapperFactory.get();
        String json = mapper.writeValueAsString(newThing);
        assert (jsonEqual(expResult, json));

        Thing parsed = mapper.readValue(expResult, Thing.class);
        String json2 = mapper.writeValueAsString(parsed);
        assert (jsonEqual(expResult, json2));
    }

    private boolean jsonEqual(String string1, String string2) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
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
