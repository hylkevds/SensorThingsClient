package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractObservedPropertyBuilder;

/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.ObservedProperty} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class ObservedPropertyBuilder extends AbstractObservedPropertyBuilder<ObservedPropertyBuilder> {

    private ObservedPropertyBuilder() {
    }

    public static ObservedPropertyBuilder builder() {
        return new ObservedPropertyBuilder();
    }

    @Override
    public ObservedPropertyBuilder getSelf() {
        return this;
    }

}
