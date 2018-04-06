package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.HistoricalLocation;
import de.fraunhofer.iosb.ilt.sta.model.Location;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import org.geojson.GeoJsonObject;

import java.util.List;

/**
 * Base class for any {@link AbstractEntityBuilder} of {@link Location}
 *
 * @param <U> the type of the concrete class that extends this {@link AbstractLocationBuilder}
 * @author Aurelien Bourdon
 */
public abstract class AbstractLocationBuilder<U extends AbstractLocationBuilder<U>> extends AbstractEntityBuilder<Location, U> {

    @Override
    protected Location newBuildingInstance() {
        return new Location();
    }

    public U name(final String name) {
        getBuildingInstance().setName(name);
        return getSelf();
    }

    public U description(final String description) {
        getBuildingInstance().setDescription(description);
        return getSelf();
    }

    public U encodingType(final ValueCode encodingType) {
        getBuildingInstance().setEncodingType(encodingType.getValue());
        return getSelf();
    }

    public U location(final Object location) {
        if (!(location instanceof GeoJsonObject)) {
            throw new BuildingException("Whereas the OGC SensorThings API specifies the Location#location as an Any type (so any Object can be used), " +
                    "the FROST-Client only accepts GeoJSONObject type");
        }
        getBuildingInstance().setLocation((GeoJsonObject) location);
        return getSelf();
    }

    public U things(final List<Thing> things) {
        getBuildingInstance().getThings().addAll(things);
        return getSelf();
    }

    public U thing(final Thing thing) {
        getBuildingInstance().getThings().add(thing);
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

    /**
     * All the possible values for a {@link Location#encodingType} attribute
     *
     * @author Aurelien Bourdon
     */
    public enum ValueCode {

        GeoJSON("application/vnd.geo+json");

        private final String value;

        ValueCode(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

}
