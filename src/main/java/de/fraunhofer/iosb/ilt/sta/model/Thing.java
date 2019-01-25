package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.ThingDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Thing extends Entity<Thing> {

    private String name;
    private String description;
    private Map<String, Object> properties;

    private final EntityList<Location> locations = new EntityList<>(EntityType.LOCATIONS);
    private final EntityList<HistoricalLocation> historicalLocations = new EntityList<>(EntityType.HISTORICAL_LOCATIONS);
    private final EntityList<Datastream> datastreams = new EntityList<>(EntityType.DATASTREAMS);
    private final EntityList<MultiDatastream> multiDatastreams = new EntityList<>(EntityType.MULTIDATASTREAMS);

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

    public Thing(String name, String description, Map<String, Object> properties, List<Location> locations,
            List<HistoricalLocation> historicalLocations, List<Datastream> datastreams) {
        this(name, description, properties);
        this.locations.addAll(locations);
        this.historicalLocations.addAll(historicalLocations);
        this.datastreams.addAll(datastreams);
    }

    @Override
    protected void ensureServiceOnChildren(SensorThingsService service) {
        locations.setService(service, Location.class);
        datastreams.setService(service, Datastream.class);
        multiDatastreams.setService(service, MultiDatastream.class);
        historicalLocations.setService(service, HistoricalLocation.class);
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
        final Thing other = (Thing) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.properties, other.properties)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + Objects.hashCode(this.properties);
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

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public BaseDao<Location> locations() {
        return getService().locations().setParent(this);
    }

    @JsonProperty("Locations")
    public EntityList<Location> getLocations() {
        return this.locations;
    }

    @JsonProperty("Locations")
    public void setLocations(List<Location> locations) {
        this.locations.replaceAll(locations);
    }

    public BaseDao<HistoricalLocation> historicalLocations() {
        return getService().historicalLocations().setParent(this);
    }

    @JsonProperty("HistoricalLocations")
    public EntityList<HistoricalLocation> getHistoricalLocations() {
        return this.historicalLocations;
    }

    @JsonProperty("HistoricalLocations")
    public void setHistoricalLocations(List<HistoricalLocation> historicalLocations) {
        this.historicalLocations.replaceAll(historicalLocations);
    }

    public BaseDao<Datastream> datastreams() {
        return getService().datastreams().setParent(this);
    }

    @JsonProperty("Datastreams")
    public EntityList<Datastream> getDatastreams() {
        return this.datastreams;
    }

    @JsonProperty("Datastreams")
    public void setDatastreams(List<Datastream> datastreams) {
        this.datastreams.replaceAll(datastreams);
    }

    public BaseDao<MultiDatastream> multiDatastreams() {
        return getService().multiDatastreams().setParent(this);
    }

    @JsonProperty("MultiDatastreams")
    public EntityList<MultiDatastream> getMultiDatastreams() {
        return this.multiDatastreams;
    }

    @JsonProperty("MultiDatastreams")
    public void setMultiDatastreams(List<MultiDatastream> multiDatastreams) {
        this.multiDatastreams.replaceAll(multiDatastreams);
    }

    @Override
    public ThingDao getDao(SensorThingsService service) {
        return new ThingDao(service);
    }

    @Override
    public Thing withOnlyId() {
        Thing copy = new Thing();
        copy.setId(id);
        return copy;
    }

    @Override
    public String toString() {
        return super.toString() + " " + getName();
    }

}
