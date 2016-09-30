package de.fraunhofer.iosb.ilt.sta.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This enum contains class and naming information about entities.
 *
 * @author Nils Sommer, Hylke van der Schaaf
 *
 */
public enum EntityType {
	DATASTREAM(Datastream.class, "Datastream", false),
	DATASTREAMS(Datastream.class, "Datastreams", true),
	FEATURE_OF_INTEREST(FeatureOfInterest.class, "FeatureOfInterest", false),
	FEATURES_OF_INTEREST(FeatureOfInterest.class, "FeaturesOfInterest", true),
	HISTORICAL_LOCATION(HistoricalLocation.class, "HistoricalLocation", false),
	HISTORICAL_LOCATIONS(HistoricalLocation.class, "HistoricalLocations", true),
	LOCATION(Location.class, "Location", false),
	LOCATIONS(Location.class, "Locations", true),
	OBSERVATION(Observation.class, "Observation", false),
	OBSERVATIONS(Observation.class, "Observations", true),
	OBSERVED_PROPERTY(ObservedProperty.class, "ObservedProperty", false),
	OBSERVED_PROPERTIES(ObservedProperty.class, "ObservedProperties", true),
	SENSOR(Sensor.class, "Sensor", false),
	SENSORS(Sensor.class, "Sensors", true),
	THING(Thing.class, "Thing", false),
	THINGS(Thing.class, "Things", true);

	private final Class<? extends Entity> type;
	private final String name;
	// Referenced as string instead of EntityType because we cannot access
	// fields before they are created :(
	private final Set<EntityType> relations;
	private final boolean isList;
	private static final Map<Class<? extends Entity>, EntityType> listForClass = new HashMap<>();
	private static final Map<Class<? extends Entity>, EntityType> singleForClass = new HashMap<>();

	static {
		DATASTREAM.addRelations(SENSOR, THING, OBSERVED_PROPERTY, OBSERVATIONS);
		DATASTREAMS.addRelations(SENSOR, THING, OBSERVED_PROPERTY, OBSERVATIONS);
		FEATURE_OF_INTEREST.addRelations(OBSERVATIONS);
		FEATURES_OF_INTEREST.addRelations(OBSERVATIONS);
		HISTORICAL_LOCATION.addRelations(THING, LOCATIONS);
		HISTORICAL_LOCATIONS.addRelations(THING, LOCATIONS);
		LOCATION.addRelations(THINGS, HISTORICAL_LOCATIONS);
		LOCATIONS.addRelations(THINGS, HISTORICAL_LOCATIONS);
		OBSERVATION.addRelations(FEATURE_OF_INTEREST, DATASTREAM);
		OBSERVATIONS.addRelations(FEATURE_OF_INTEREST, DATASTREAM);
		OBSERVED_PROPERTY.addRelations(DATASTREAMS);
		OBSERVED_PROPERTIES.addRelations(DATASTREAMS);
		SENSOR.addRelations(DATASTREAMS);
		SENSORS.addRelations(DATASTREAMS);
		THING.addRelations(DATASTREAMS, LOCATIONS, HISTORICAL_LOCATIONS);
		THINGS.addRelations(DATASTREAMS, LOCATIONS, HISTORICAL_LOCATIONS);

		for (EntityType type : values()) {
			if (type.isList) {
				listForClass.put(type.type, type);
			} else {
				singleForClass.put(type.type, type);
			}
		}
	}

	EntityType(Class<? extends Entity> type, String name, boolean isList) {
		this.type = type;
		this.name = name;
		this.isList = isList;
		this.relations = new HashSet<>();
	}

	private void addRelations(EntityType... relations) {
		this.relations.addAll(Arrays.asList(relations));
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

	/**
	 * Check whether this entity type has a relationship to another entity type.
	 *
	 * @param other the other entity type
	 * @return true if they are related, otherwise false
	 */
	public boolean hasRelationTo(EntityType other) {
		return this.relations.contains(other);
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
		}

		return null;
	}
}
