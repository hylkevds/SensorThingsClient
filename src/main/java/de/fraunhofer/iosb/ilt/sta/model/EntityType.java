package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.core.type.TypeReference;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This enum contains class and naming information about entities.
 *
 * @author Nils Sommer, Hylke van der Schaaf, Michael Jacoby
 *
 */
public enum EntityType {
    DATASTREAM(Datastream.class, new TypeReference<Datastream>() {
    }, "Datastream", false),
    DATASTREAMS(Datastream.class, new TypeReference<EntityList<Datastream>>() {
    }, "Datastreams", true),
    MULTIDATASTREAM(MultiDatastream.class, new TypeReference<MultiDatastream>() {
    }, "MultiDatastream", false),
    MULTIDATASTREAMS(MultiDatastream.class, new TypeReference<EntityList<MultiDatastream>>() {
    }, "MultiDatastreams", true),
    FEATURE_OF_INTEREST(FeatureOfInterest.class, new TypeReference<FeatureOfInterest>() {
    }, "FeatureOfInterest", false),
    FEATURES_OF_INTEREST(FeatureOfInterest.class, new TypeReference<EntityList<FeatureOfInterest>>() {
    }, "FeaturesOfInterest", true),
    HISTORICAL_LOCATION(HistoricalLocation.class, new TypeReference<HistoricalLocation>() {
    }, "HistoricalLocation", false),
    HISTORICAL_LOCATIONS(HistoricalLocation.class, new TypeReference<EntityList<HistoricalLocation>>() {
    }, "HistoricalLocations", true),
    LOCATION(Location.class, new TypeReference<Location>() {
    }, "Location", false),
    LOCATIONS(Location.class, new TypeReference<EntityList<Location>>() {
    }, "Locations", true),
    OBSERVATION(Observation.class, new TypeReference<Observation>() {
    }, "Observation", false),
    OBSERVATIONS(Observation.class, new TypeReference<EntityList<Observation>>() {
    }, "Observations", true),
    OBSERVED_PROPERTY(ObservedProperty.class, new TypeReference<ObservedProperty>() {
    }, "ObservedProperty", false),
    OBSERVED_PROPERTIES(ObservedProperty.class, new TypeReference<EntityList<ObservedProperty>>() {
    }, "ObservedProperties", true),
    SENSOR(Sensor.class, new TypeReference<Sensor>() {
    }, "Sensor", false),
    SENSORS(Sensor.class, new TypeReference<EntityList<Sensor>>() {
    }, "Sensors", true),
    THING(Thing.class, new TypeReference<Thing>() {
    }, "Thing", false),
    THINGS(Thing.class, new TypeReference<EntityList<Thing>>() {
    }, "Things", true),
    ACTUATOR(Actuator.class, new TypeReference<Actuator>() {
    }, "Actuator", false),
    ACTUATORS(Actuator.class, new TypeReference<EntityList<Actuator>>() {
    }, "Actuators", true),
    TASKING_CAPABILITY(TaskingCapability.class, new TypeReference<TaskingCapability>() {
    }, "TaskingCapability", false),
    TASKING_CAPABILITIES(TaskingCapability.class, new TypeReference<EntityList<TaskingCapability>>() {
    }, "TaskingCapabilities", true),
    TASK(Task.class, new TypeReference<Task>() {
    }, "Task", false),
    TASKS(Task.class, new TypeReference<EntityList<Task>>() {
    }, "Tasks", true);

    public Set<EntityProperty> getProperties() {
        return properties;
    }

    private final Class<? extends Entity> type;
    private final TypeReference typeReference;
    private final String name;
    private final Set<EntityType> relations;
    private final Set<EntityProperty> properties;
    private final boolean isList;
    /**
     * The other one of a singular/plural pair.
     */
    private EntityType other;
    private static final Map<Class<? extends Entity>, EntityType> listForClass = new HashMap<>();
    private static final Map<Class<? extends Entity>, EntityType> singleForClass = new HashMap<>();

    static {
        ACTUATOR.setOther(ACTUATORS);
        ACTUATORS.setOther(ACTUATOR);
        DATASTREAM.setOther(DATASTREAMS);
        DATASTREAMS.setOther(DATASTREAM);
        FEATURE_OF_INTEREST.setOther(FEATURES_OF_INTEREST);
        FEATURES_OF_INTEREST.setOther(FEATURE_OF_INTEREST);
        HISTORICAL_LOCATION.setOther(HISTORICAL_LOCATIONS);
        HISTORICAL_LOCATIONS.setOther(HISTORICAL_LOCATION);
        LOCATION.setOther(LOCATIONS);
        LOCATIONS.setOther(LOCATION);
        MULTIDATASTREAM.setOther(MULTIDATASTREAMS);
        MULTIDATASTREAMS.setOther(MULTIDATASTREAM);
        OBSERVATION.setOther(OBSERVATIONS);
        OBSERVATIONS.setOther(OBSERVATION);
        OBSERVED_PROPERTY.setOther(OBSERVED_PROPERTIES);
        OBSERVED_PROPERTIES.setOther(OBSERVED_PROPERTY);
        SENSOR.setOther(SENSORS);
        SENSORS.setOther(SENSOR);
        TASK.setOther(TASKS);
        TASKS.setOther(TASK);
        TASKING_CAPABILITY.setOther(TASKING_CAPABILITIES);
        TASKING_CAPABILITIES.setOther(TASKING_CAPABILITY);
        THING.setOther(THINGS);
        THINGS.setOther(THING);

        ACTUATOR.addRelations(TASKING_CAPABILITIES);
        ACTUATORS.addRelations(TASKING_CAPABILITIES);
        DATASTREAM.addRelations(SENSOR, THING, OBSERVED_PROPERTY, OBSERVATIONS);
        DATASTREAMS.addRelations(SENSOR, THING, OBSERVED_PROPERTY, OBSERVATIONS);
        FEATURE_OF_INTEREST.addRelations(OBSERVATIONS);
        FEATURES_OF_INTEREST.addRelations(OBSERVATIONS);
        HISTORICAL_LOCATION.addRelations(THING, LOCATIONS);
        HISTORICAL_LOCATIONS.addRelations(THING, LOCATIONS);
        LOCATION.addRelations(THINGS, HISTORICAL_LOCATIONS);
        LOCATIONS.addRelations(THINGS, HISTORICAL_LOCATIONS);
        MULTIDATASTREAM.addRelations(SENSOR, THING, OBSERVED_PROPERTIES, OBSERVATIONS);
        MULTIDATASTREAMS.addRelations(SENSOR, THING, OBSERVED_PROPERTIES, OBSERVATIONS);
        OBSERVATION.addRelations(FEATURE_OF_INTEREST, DATASTREAM, MULTIDATASTREAM);
        OBSERVATIONS.addRelations(FEATURE_OF_INTEREST, DATASTREAM, MULTIDATASTREAM);
        OBSERVED_PROPERTY.addRelations(DATASTREAMS, MULTIDATASTREAMS);
        OBSERVED_PROPERTIES.addRelations(DATASTREAMS, MULTIDATASTREAMS);
        SENSOR.addRelations(DATASTREAMS, MULTIDATASTREAMS);
        SENSORS.addRelations(DATASTREAMS, MULTIDATASTREAMS);
        TASK.addRelations(TASKING_CAPABILITY);
        TASKS.addRelations(TASKING_CAPABILITY);
        TASKING_CAPABILITY.addRelations(ACTUATOR, TASKS, THING);
        TASKING_CAPABILITIES.addRelations(ACTUATOR, TASKS, THING);
        THING.addRelations(DATASTREAMS, MULTIDATASTREAMS, LOCATIONS, HISTORICAL_LOCATIONS, TASKING_CAPABILITIES);
        THINGS.addRelations(DATASTREAMS, MULTIDATASTREAMS, LOCATIONS, HISTORICAL_LOCATIONS, TASKING_CAPABILITIES);

        ACTUATOR.addProperties(EntityProperty.ID, EntityProperty.PROPERTIES, EntityProperty.NAME, EntityProperty.DESCRIPTION, EntityProperty.ENCODINGTYPE, EntityProperty.METADATA);
        DATASTREAM.addProperties(EntityProperty.ID, EntityProperty.PROPERTIES, EntityProperty.NAME, EntityProperty.DESCRIPTION, EntityProperty.OBSERVATIONTYPE, EntityProperty.UNITOFMEASUREMENT, EntityProperty.OBSERVEDAREA, EntityProperty.PHENOMENONTIME, EntityProperty.RESULTTIME);
        FEATURE_OF_INTEREST.addProperties(EntityProperty.ID, EntityProperty.NAME, EntityProperty.DESCRIPTION, EntityProperty.ENCODINGTYPE, EntityProperty.FEATURE);
        HISTORICAL_LOCATION.addProperties(EntityProperty.ID, EntityProperty.TIME);
        LOCATION.addProperties(EntityProperty.ID, EntityProperty.PROPERTIES, EntityProperty.NAME, EntityProperty.DESCRIPTION, EntityProperty.ENCODINGTYPE, EntityProperty.LOCATION);
        MULTIDATASTREAM.addProperties(EntityProperty.ID, EntityProperty.PROPERTIES, EntityProperty.NAME, EntityProperty.DESCRIPTION, EntityProperty.OBSERVATIONTYPE, EntityProperty.MULTIOBSERVATIONDATATYPES, EntityProperty.UNITOFMEASUREMENTS, EntityProperty.OBSERVEDAREA, EntityProperty.PHENOMENONTIME, EntityProperty.RESULTTIME);
        OBSERVATION.addProperties(EntityProperty.ID, EntityProperty.PHENOMENONTIME, EntityProperty.RESULTTIME, EntityProperty.RESULT, EntityProperty.RESULTQUALITY, EntityProperty.VALIDTIME, EntityProperty.PARAMETERS);
        OBSERVED_PROPERTY.addProperties(EntityProperty.ID, EntityProperty.PROPERTIES, EntityProperty.NAME, EntityProperty.DESCRIPTION, EntityProperty.DEFINITION);
        SENSOR.addProperties(EntityProperty.ID, EntityProperty.PROPERTIES, EntityProperty.NAME, EntityProperty.DESCRIPTION, EntityProperty.ENCODINGTYPE, EntityProperty.METADATA);
        TASK.addProperties(EntityProperty.ID, EntityProperty.CREATIONTIME, EntityProperty.TASKINGPARAMETERS);
        TASKING_CAPABILITY.addProperties(EntityProperty.ID, EntityProperty.PROPERTIES, EntityProperty.NAME, EntityProperty.DESCRIPTION, EntityProperty.TASKINGPARAMETERS);
        THING.addProperties(EntityProperty.ID, EntityProperty.PROPERTIES, EntityProperty.NAME, EntityProperty.DESCRIPTION);

        for (EntityType type : values()) {
            if (type.isList) {
                listForClass.put(type.type, type);
            } else {
                singleForClass.put(type.type, type);
            }
        }
    }

    EntityType(Class<? extends Entity> type, TypeReference typeReference, String name, boolean isList) {
        this.type = type;
        this.typeReference = typeReference;
        this.name = name;
        this.isList = isList;
        this.relations = new HashSet<>();
        this.properties = new HashSet<>();
    }

    private void addRelations(EntityType... relations) {
        this.relations.addAll(Arrays.asList(relations));
    }

    private void addProperties(EntityProperty... properties) {
        this.getProperties().addAll(Arrays.asList(properties));
    }

    /**
     * Get the class of this entity type.
     *
     * @return the class
     */
    public Class<? extends Entity> getType() {
        return this.type;
    }

    /**
     * Returns the typeReference used for this entity type. For plurals, it
     * returns the typeReference for the collection of the type.
     *
     * @return The type reference.
     */
    public TypeReference getTypeReference() {
        return typeReference;
    }

    /**
     * Get the name of this entity type.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    public boolean isList() {
        return isList;
    }

    private void setOther(EntityType other) {
        this.other = other;
    }

    public EntityType getPlural() {
        if (isList) {
            return this;
        }
        return other;
    }

    public EntityType getSingular() {
        if (!isList) {
            return this;
        }
        return other;
    }

    /**
     * Check whether this entity type has a relationship to another entity type.
     *
     * @param other the other entity type
     * @return true if they are related, otherwise false
     */
    public boolean hasRelationTo(EntityType other) {
        return this.relations.contains(other);
    }

    public boolean hasProperty(EntityProperty property) {
        return this.getProperties().contains(property);
    }

    public static EntityType listForClass(Class<? extends Entity> clazz) {
        return listForClass.get(clazz);
    }

    public static EntityType singleForClass(Class<? extends Entity> clazz) {
        return singleForClass.get(clazz);
    }

    /**
     * Get an EntityType by name.
     *
     * @param name the name
     * @return the EntityType
     */
    public static EntityType byName(String name) {
        switch (name) {
            case "Datastream":
                return DATASTREAM;
            case "Datastreams":
                return DATASTREAMS;
            case "FeatureOfInterest":
                return FEATURE_OF_INTEREST;
            case "FeaturesOfInterest":
                return FEATURES_OF_INTEREST;
            case "HistoricalLocation":
                return HISTORICAL_LOCATION;
            case "HistoricalLocations":
                return HISTORICAL_LOCATIONS;
            case "Location":
                return LOCATION;
            case "Locations":
                return LOCATIONS;
            case "MultiDatastream":
                return MULTIDATASTREAM;
            case "MultiDatastreams":
                return MULTIDATASTREAMS;
            case "Observation":
                return OBSERVATION;
            case "Observations":
                return OBSERVATIONS;
            case "ObservedProperty":
                return OBSERVED_PROPERTY;
            case "ObservedProperties":
                return OBSERVED_PROPERTIES;
            case "Sensor":
                return SENSOR;
            case "Sensors":
                return SENSORS;
            case "Thing":
                return THING;
            case "Things":
                return THINGS;
            case "Actuator":
                return ACTUATOR;
            case "Actuators":
                return ACTUATORS;
            case "Task":
                return TASK;
            case "Tasks":
                return TASKS;
            case "TaskingCapability":
                return TASKING_CAPABILITY;
            case "TaskingCapabilities":
                return TASKING_CAPABILITIES;
        }

        return null;
    }
}
