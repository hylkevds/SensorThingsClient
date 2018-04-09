package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.HistoricalLocation;
import de.fraunhofer.iosb.ilt.sta.model.Location;
import de.fraunhofer.iosb.ilt.sta.model.MultiDatastream;
import de.fraunhofer.iosb.ilt.sta.model.Thing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for any {@link EntityBuilder} of {@link Thing}
 *
 * @param <U> the type of the concrete class that extends this {@link AbstractThingBuilder}
 * @author Aurelien Bourdon
 */
public abstract class AbstractThingBuilder<U extends AbstractThingBuilder<U>> extends EntityBuilder<Thing, U> {

    @Override
    protected Thing newBuildingInstance() {
        return new Thing();
    }

    public U name(final String name) {
        getBuildingInstance().setName(name);
        return getSelf();
    }

    public U description(final String description) {
        getBuildingInstance().setDescription(description);
        return getSelf();
    }

    public U properties(final Map<String, Object> properties) {
        getBuildingInstance().setProperties(properties);
        return getSelf();
    }

    public U property(final String key, final Object value) {
        if (getBuildingInstance().getProperties() == null) {
            getBuildingInstance().setProperties(new HashMap<>());
        }
        getBuildingInstance().getProperties().put(key, value);
        return getSelf();
    }

    public U datastreams(final List<Datastream> datastreams) {
        getBuildingInstance().getDatastreams().addAll(datastreams);
        return getSelf();
    }

    public U multiDatastreams(final List<MultiDatastream> multiDatastreams) {
        getBuildingInstance().getMultiDatastreams().addAll(multiDatastreams);
        return getSelf();
    }

    public U multiDatastream(final MultiDatastream multiDatastream) {
        getBuildingInstance().getMultiDatastreams().add(multiDatastream);
        return getSelf();
    }

    public U datastream(final Datastream datastream) {
        getBuildingInstance().getDatastreams().add(datastream);
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

    public U historicalLocations(final List<HistoricalLocation> historicalLocations) {
        getBuildingInstance().getHistoricalLocations().addAll(historicalLocations);
        return getSelf();
    }

    public U historicalLocation(final HistoricalLocation historicalLocation) {
        getBuildingInstance().getHistoricalLocations().add(historicalLocation);
        return getSelf();
    }

}
