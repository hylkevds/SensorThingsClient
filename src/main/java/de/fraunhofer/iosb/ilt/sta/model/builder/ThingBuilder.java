package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractThingBuilder;

/**
 * Default {@link de.fraunhofer.iosb.ilt.sta.model.Thing} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class ThingBuilder extends AbstractThingBuilder<ThingBuilder> {

    private ThingBuilder() {
    }

    public static ThingBuilder builder() {
        return new ThingBuilder();
    }

    @Override
    public ThingBuilder getSelf() {
        return this;
    }

}
