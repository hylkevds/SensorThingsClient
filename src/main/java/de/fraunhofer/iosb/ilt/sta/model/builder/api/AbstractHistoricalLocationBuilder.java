package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.HistoricalLocation;
import de.fraunhofer.iosb.ilt.sta.model.Location;
import de.fraunhofer.iosb.ilt.sta.model.Thing;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class AbstractHistoricalLocationBuilder<U extends AbstractHistoricalLocationBuilder<U>> extends AbstractEntityBuilder<HistoricalLocation, U> {

    @Override
    protected HistoricalLocation newBuildingInstance() {
        return new HistoricalLocation();
    }

    public U time(final ZonedDateTime time) {
        getBuildingInstance().setTime(time);
        return getSelf();
    }

    public U thing(final Thing thing) {
        getBuildingInstance().setThing(thing);
        return getSelf();
    }

    public U locations(final List<Location> locations) {
        getBuildingInstance().getLocations().addAll(locations);
        return getSelf();
    }

    public U location(final Location location) {
        getBuildingInstance().getLocations().add(location);
        return getSelf();
    }

}
