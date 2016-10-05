package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.ThingDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.util.Map;

public class Thing extends Entity<Thing> {

	private String name;
	private String description;
	private Map<String, Object> properties;

	@JsonProperty("Locations")
	private EntityList<Location> locations;

	@JsonProperty("HistoricalLocations")
	private EntityList<HistoricalLocation> historicalLocations;

	@JsonProperty("Datastreams")
	private EntityList<Datastream> datastreams;

	public Thing() {
		super(EntityType.THING);
	}

	public Thing(String name, String description) {
		this();
		this.name = name;
		this.description = description;
	}

	public Thing(String name, String description, Map<String, Object> properties) {
		this(name, description);
		this.properties = properties;
	}

	public Thing(String name, String description, Map<String, Object> properties, EntityList<Location> locations,
			EntityList<HistoricalLocation> historicalLocations, EntityList<Datastream> datastreams) {
		this(name, description, properties);
		this.locations = locations;
		this.historicalLocations = historicalLocations;
		this.datastreams = datastreams;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public BaseDao<Location> locations() {
		return getService().locations().setParent(this);
	}

	public EntityList<Location> getLocations() {
		return this.locations;
	}

	public void setLocations(EntityList<Location> locations) {
		this.locations = locations;
	}

	public EntityList<HistoricalLocation> getHistoricalLocations() {
		return this.historicalLocations;
	}

	public void setHistoricalLocations(EntityList<HistoricalLocation> historicalLocations) {
		this.historicalLocations = historicalLocations;
	}

	public BaseDao<Datastream> datastreams() {
		return getService().datastreams().setParent(this);
	}

	public EntityList<Datastream> getDatastreams() {
		return this.datastreams;
	}

	public void setDatastreams(EntityList<Datastream> datastreams) {
		this.datastreams = datastreams;
	}

	@Override
	public ThingDao getDao(SensorThingsService service) {
		return new ThingDao(service);
	}

}
