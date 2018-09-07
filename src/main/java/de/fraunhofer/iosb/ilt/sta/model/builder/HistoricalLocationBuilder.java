package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractHistoricalLocationBuilder;

/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.HistoricalLocation} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class HistoricalLocationBuilder extends AbstractHistoricalLocationBuilder<HistoricalLocationBuilder> {

    private HistoricalLocationBuilder() {
    }

    public static HistoricalLocationBuilder builder() {
        return new HistoricalLocationBuilder();
    }

    @Override
    public HistoricalLocationBuilder getSelf() {
        return this;
    }

}
