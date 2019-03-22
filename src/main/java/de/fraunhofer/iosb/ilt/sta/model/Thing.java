package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.ThingDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.util.Map;
import java.util.Objects;

public class Thing extends Entity<Thing> {

    private String name;
    private String description;
    private Map<String, Object> properties;

    @JsonProperty("Locations")
    private EntityList<Location> locations = new EntityList<>(EntityType.LOCATIONS);

    @JsonProperty("HistoricalLocations")
    private EntityList<HistoricalLocation> historicalLocations = new EntityList<>(EntityType.HISTORICAL_LOCATIONS);

    @JsonProperty("Datastreams")
    private EntityList<Datastream> datastreams = new EntityList<>(EntityType.DATASTREAMS);

    @JsonProperty("MultiDatastreams")
    private EntityList<MultiDatastream> multiDatastreams = new EntityList<>(EntityType.MULTIDATASTREAMS);

    @JsonProperty("TaskingCapabilities")
    private EntityList<TaskingCapability> taskingCapabilities = new EntityList<>(EntityType.TASKING_CAPABILITIES);

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

    // TODO: add more constructors?

    @Override
    protected void ensureServiceOnChildren(SensorThingsService service) {
        locations.setService(service, Location.class);
        datastreams.setService(service, Datastream.class);
        multiDatastreams.setService(service, MultiDatastream.class);
        historicalLocations.setService(service, HistoricalLocation.class);
        taskingCapabilities.setService(service, TaskingCapability.class);
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

    public EntityList<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(EntityList<Location> locations) {
        this.locations = locations;
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

    public BaseDao<Datastream> datastreams() {
        return getService().datastreams().setParent(this);
    }

    public EntityList<Datastream> getDatastreams() {
        return this.datastreams;
    }

    public void setDatastreams(EntityList<Datastream> datastreams) {
        this.datastreams = datastreams;
    }

    public BaseDao<MultiDatastream> multiDatastreams() {
        return getService().multiDatastreams().setParent(this);
    }

    public EntityList<MultiDatastream> getMultiDatastreams() {
        return this.multiDatastreams;
    }

    public void setMultiDatastreams(EntityList<MultiDatastream> multiDatastreams) {
        this.multiDatastreams = multiDatastreams;
    }

    public BaseDao<TaskingCapability> taskingCapabilities() {
        return getService().taskingCapabilities().setParent(this);
    }

    public EntityList<TaskingCapability> getTaskingCapabilities() {
        return this.taskingCapabilities;
    }

    public void setTaskingCapabilities(EntityList<TaskingCapability> taskingCapabilities) {
        this.taskingCapabilities = taskingCapabilities;
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
