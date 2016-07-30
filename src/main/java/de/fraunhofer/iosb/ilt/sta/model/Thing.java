package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
	}

	public Thing(String name, String description, Map<String, Object> properties) {
		this.name = name;
		this.description = description;
		this.properties = properties;
	}

	public Thing(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Thing(String description, Map<String, Object> properties, EntityList<Location> locations,
			EntityList<HistoricalLocation> historicalLocations, EntityList<Datastream> datastreams) {
		super();
		this.description = description;
		this.properties = properties;
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
