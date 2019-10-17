/*
 * Copyright (C) 2018 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
 * Karlsruhe, Germany.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fraunhofer.iosb.ilt.sta.model.builder.api.ext;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.ExtensibleBuilder;
import de.fraunhofer.iosb.ilt.swe.common.constraint.AllowedValues;
import de.fraunhofer.iosb.ilt.swe.common.simple.Count;
import de.fraunhofer.iosb.ilt.swe.common.simple.Quantity;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * /**
 * Base class for any {@link Count} builder.
 * <p>
 * Any {@link AbstractQuantityBuilder} is an {@link ExtensibleBuilder}.
 *
 * @param <T> the concrete {@link Quantity} type to build
 * @author Michael Jacoby
 */
public abstract class AbstractQuantityBuilder<T extends AbstractQuantityBuilder<T>> extends AbstractSimpleComponentBuilder<Quantity, T> {

    @Override
    protected Quantity newBuildingInstance() {
        return new Quantity();
    }

    public T value(final BigDecimal value) {
        getBuildingInstance().setValue(value);
        return getSelf();
    }

    public T constraint(final AllowedValues allowedValues) {
        getBuildingInstance().setConstraint(allowedValues);
        return getSelf();
    }

    public T allowedValue(final BigDecimal value) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedValues());
        }
        getBuildingInstance().getConstraint().getValue().add(value);
        return getSelf();
    }

    public T allowedValues(final List<BigDecimal> values) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedValues());
        }
        getBuildingInstance().getConstraint().getValue().addAll(values);
        return getSelf();
    }

    public T allowedValues(final BigDecimal... values) {
        return allowedValues(Arrays.asList(values));
    }

    public T interval(final List<BigDecimal> interval) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedValues());
        }
        getBuildingInstance().getConstraint().getInterval().add(interval);
        return getSelf();
    }

    public T interval(final BigDecimal start, final BigDecimal end) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedValues());
        }
        getBuildingInstance().getConstraint().getInterval().add(Arrays.asList(start, end));
        return getSelf();
    }

    public T intervals(final List<List<BigDecimal>> intervals) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedValues());
        }
        getBuildingInstance().getConstraint().setInterval(intervals);
        return getSelf();
    }

    public T significantFigures(final int significantFigures) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedValues());
        }
        getBuildingInstance().getConstraint().setSignificantFigures(significantFigures);
        return getSelf();
    }

    public T uom(final String uom) {
        getBuildingInstance().setUom(uom);
        return getSelf();
    }
}
