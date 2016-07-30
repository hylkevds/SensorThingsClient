package de.fraunhofer.iosb.ilt.sta.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This enum contains class and naming information about entities.
 *
 * @author Nils Sommer
 *
 */
public enum EntityType {
	DATASTREAM(Datastream.class, "Datastream", false, "Sensor", "Thing", "ObservedProperty", "Observations"),
	DATASTREAMS(Datastream.class, "Datastreams", true, "Sensor", "Thing", "ObservedProperty", "Observations"),
	FEATURE_OF_INTEREST(FeatureOfInterest.class, "FeatureOfInterest", false, "Observations"),
	FEATURES_OF_INTEREST(FeatureOfInterest.class, "FeaturesOfInterest", true, "Observations"),
	HISTORICAL_LOCATION(HistoricalLocation.class, "HistoricalLocation", false, "Thing", "Locations"),
	HISTORICAL_LOCATIONS(HistoricalLocation.class, "HistoricalLocations", true, "Thing", "Locations"),
	LOCATION(Location.class, "Location", false, "Things", "HistoricalLocations"),
	LOCATIONS(Location.class, "Locations", true, "Things", "HistoricalLocations"),
	OBSERVATION(Observation.class, "Observation", false, "FeatureOfInterest", "Datastream"),
	OBSERVATIONS(Observation.class, "Observations", true, "FeatureOfInterest", "Datastream"),
	OBSERVED_PROPERTY(ObservedProperty.class, "ObservedProperty", false, "Datastreams"),
	OBSERVED_PROPERTIES(ObservedProperty.class, "ObservedProperties", true, "Datastreams"),
	SENSOR(Sensor.class, "Sensor", false, "Datastreams"),
	SENSORS(Sensor.class, "Sensors", true, "Datastreams"),
	THING(Thing.class, "Thing", false, "Datastreams", "Locations", "HistoricalLocations"),
	THINGS(Thing.class, "Things", true, "Datastreams", "Locations", "HistoricalLocations");

	private final Class<? extends Entity> type;
	private final String name;
	// Referenced as string instead of EntityType because we cannot access
	// fields before they are created :(
	private final Set<String> relations;
	private final boolean isList;
	private static final Map<Class<? extends Entity>, EntityType> listForClass = new HashMap<>();

	static {
		for (EntityType type : values()) {
			if (type.isList) {
				listForClass.put(type.type, type);
			}
		}
	}

	EntityType(Class<? extends Entity> type, String name, boolean isList, String... relations) {
		this.type = type;
		this.name = name;
		this.isList = isList;
		this.relations = new HashSet<>();
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
		return this.relations.contains(other.getName());
	}

	public static EntityType listForClass(Class<? extends Entity> clazz) {
		return listForClass.get(clazz);
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
