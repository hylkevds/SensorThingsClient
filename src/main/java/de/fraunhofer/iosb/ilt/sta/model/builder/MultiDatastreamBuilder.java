package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractMultiDatastreamBuilder;

/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.MultiDatastream} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class MultiDatastreamBuilder extends AbstractMultiDatastreamBuilder<MultiDatastreamBuilder> {

    private MultiDatastreamBuilder() {
    }

    public static MultiDatastreamBuilder builder() {
        return new MultiDatastreamBuilder();
    }

    @Override
    public MultiDatastreamBuilder getSelf() {
        return this;
    }

}
