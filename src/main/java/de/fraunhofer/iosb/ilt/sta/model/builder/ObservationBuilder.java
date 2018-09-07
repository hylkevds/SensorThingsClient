package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractObservationBuilder;

/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.Observation} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class ObservationBuilder extends AbstractObservationBuilder<ObservationBuilder> {

    private ObservationBuilder() {
    }

    public static ObservationBuilder builder() {
        return new ObservationBuilder();
    }

    @Override
    public ObservationBuilder getSelf() {
        return this;
    }

}
