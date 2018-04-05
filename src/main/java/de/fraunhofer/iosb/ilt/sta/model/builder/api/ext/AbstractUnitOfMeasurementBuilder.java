package de.fraunhofer.iosb.ilt.sta.model.builder.api.ext;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.ExtensibleBuilder;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;

/**
 * Base class for any {@link UnitOfMeasurement} builder
 *
 * @param <T> the concrete type that extends this {@link AbstractUnitOfMeasurementBuilder}
 * @author Aurelien Bourdon
 */
public abstract class AbstractUnitOfMeasurementBuilder<T extends AbstractUnitOfMeasurementBuilder<T>> extends ExtensibleBuilder<UnitOfMeasurement, T> {

    @Override
    protected UnitOfMeasurement newBuildingInstance() {
        return new UnitOfMeasurement();
    }

    public T name(final String name) {
        getBuildingInstance().setName(name);
        return getSelf();
    }

    public T definition(final String definition) {
        getBuildingInstance().setDefinition(definition);
        return getSelf();
    }

    public T symbol(final String symbol) {
        getBuildingInstance().setSymbol(symbol);
        return getSelf();
    }

}
