package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractLocationBuilder;

/**
 * Default {@link de.fraunhofer.iosb.ilt.sta.model.Location} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class LocationBuilder extends AbstractLocationBuilder<LocationBuilder> {

    private LocationBuilder() {
    }

    public static LocationBuilder builder() {
        return new LocationBuilder();
    }

    @Override
    public LocationBuilder getSelf() {
        return this;
    }

}
