package de.fraunhofer.iosb.ilt.sta.model.builder.ext;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.ext.AbstractUnitOfMeasurementBuilder;

/**
 * Default {@link de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class UnitOfMeasurementBuilder extends AbstractUnitOfMeasurementBuilder<UnitOfMeasurementBuilder> {

    private UnitOfMeasurementBuilder() {
    }

    public static UnitOfMeasurementBuilder builder() {
        return new UnitOfMeasurementBuilder();
    }

    @Override
    protected UnitOfMeasurementBuilder getSelf() {
        return this;
    }

}
