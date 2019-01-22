package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.HistoricalLocationDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.time.ZonedDateTime;
import java.util.Objects;

public class HistoricalLocation extends Entity<HistoricalLocation> {

    private ZonedDateTime time;

    @JsonProperty("Locations")
    private final EntityList<Location> locations = new EntityList<>(EntityType.LOCATIONS);

    @JsonProperty("Thing")
    private Thing thing;

    public HistoricalLocation() {
        super(EntityType.HISTORICAL_LOCATION);
    }

    public HistoricalLocation(ZonedDateTime time) {
        this();
        this.time = time;
    }

    @Override
    protected void ensureServiceOnChildren(SensorThingsService service) {
        if (thing != null) {
            thing.setService(service);
        }
        locations.setService(service, Location.class);
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
        final HistoricalLocation other = (HistoricalLocation) obj;
        if (!Objects.equals(this.time, other.time)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 53 * hash + Objects.hashCode(this.time);
        return hash;
    }

    public ZonedDateTime getTime() {
        return this.time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public BaseDao<Location> locations() {
        return getService().locations().setParent(this);
    }

    public EntityList<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(EntityList<Location> locations) {
        this.locations.replaceAll(locations);
    }

    public Thing getThing() throws ServiceFailureException {
        if (thing == null && getService() != null) {
            thing = getService().things().find(this);
        }
        return this.thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    @Override
    public BaseDao<HistoricalLocation> getDao(SensorThingsService service) {
        return new HistoricalLocationDao(service);
    }

    @Override
    public HistoricalLocation withOnlyId() {
        HistoricalLocation copy = new HistoricalLocation();
        copy.setId(id);
        return copy;
    }

    @Override
    public String toString() {
        return super.toString() + " " + time.toString();
    }
}
