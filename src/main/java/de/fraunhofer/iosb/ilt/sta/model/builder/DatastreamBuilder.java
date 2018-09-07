package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractDatastreamBuilder;

/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.Datastream} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class DatastreamBuilder extends AbstractDatastreamBuilder<DatastreamBuilder> {

    private DatastreamBuilder() {
    }

    public static DatastreamBuilder builder() {
        return new DatastreamBuilder();
    }

    @Override
    public DatastreamBuilder getSelf() {
        return this;
    }

}
