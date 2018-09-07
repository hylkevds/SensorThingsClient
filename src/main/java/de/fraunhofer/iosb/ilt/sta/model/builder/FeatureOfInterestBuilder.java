package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractFeatureOfInterestBuilder;

/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.FeatureOfInterest} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class FeatureOfInterestBuilder extends AbstractFeatureOfInterestBuilder<FeatureOfInterestBuilder> {

    private FeatureOfInterestBuilder() {
    }

    public static FeatureOfInterestBuilder builder() {
        return new FeatureOfInterestBuilder();
    }

    @Override
    public FeatureOfInterestBuilder getSelf() {
        return this;
    }

}
