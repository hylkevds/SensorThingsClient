package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.LocationDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.util.Objects;
import org.geojson.GeoJsonObject;

public class Location extends Entity<Location> {

	private String name;
	private String description;
	private String encodingType;
	private GeoJsonObject location;

	@JsonProperty("Things")
	private EntityList<Thing> things = new EntityList<>(EntityType.THINGS);

	@JsonProperty("HistoricalLocations")
	private EntityList<HistoricalLocation> historicalLocations = new EntityList<>(EntityType.HISTORICAL_LOCATIONS);

	public Location() {
		super(EntityType.LOCATION);
	}

	public Location(String name, String description, String encodingType, GeoJsonObject location) {
		this();
		this.name = name;
		this.description = description;
		this.encodingType = encodingType;
		this.location = location;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Location other = (Location) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.description, other.description)) {
			return false;
		}
		if (!Objects.equals(this.encodingType, other.encodingType)) {
			return false;
		}
		if (!Objects.equals(this.location, other.location)) {
			return false;
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 59 * hash + Objects.hashCode(this.name);
		hash = 59 * hash + Objects.hashCode(this.description);
		hash = 59 * hash + Objects.hashCode(this.encodingType);
		hash = 59 * hash + Objects.hashCode(this.location);
		return hash;
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

	public String getEncodingType() {
		return this.encodingType;
	}

	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}

	public BaseDao<HistoricalLocation> historicalLocations() {
		return getService().historicalLocations().setParent(this);
	}

	public EntityList<HistoricalLocation> getHistoricalLocations() {
		return this.historicalLocations;
	}

	public void setHistoricalLocations(EntityList<HistoricalLocation> historicalLocations) {
		this.historicalLocations = historicalLocations;
	}

	public GeoJsonObject getLocation() {
		return this.location;
	}

	public void setLocation(GeoJsonObject location) {
		this.location = location;
	}

	public BaseDao<Thing> things() {
		return getService().things().setParent(this);
	}

	public EntityList<Thing> getThings() {
		if (this.things == null) {
			this.things = new EntityList<>(EntityType.THINGS);

		}
		return this.things;
	}

	public void setThings(EntityList<Thing> things) {
		this.things = things;
	}

	@Override
	public BaseDao<Location> getDao(SensorThingsService service) {
		return new LocationDao(service);
	}

	@Override
	public Location withOnlyId() {
		Location copy = new Location();
		copy.setId(id);
		return copy;
	}
}
