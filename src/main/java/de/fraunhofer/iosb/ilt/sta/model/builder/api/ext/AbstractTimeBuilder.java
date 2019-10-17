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
import de.fraunhofer.iosb.ilt.swe.common.constraint.AllowedTimes;
import de.fraunhofer.iosb.ilt.swe.common.simple.Time;
import java.util.Arrays;
import java.util.List;

/**
 * /**
 * Base class for any {@link Time} builder.
 * <p>
 * Any {@link AbstractTimeBuilder} is an {@link ExtensibleBuilder}.
 *
 * @param <T> the concrete {@link Time} type to build
 * @author Michael Jacoby
 */
public abstract class AbstractTimeBuilder<T extends AbstractTimeBuilder<T>> extends AbstractSimpleComponentBuilder<Time, T> {

    @Override
    protected Time newBuildingInstance() {
        return new Time();
    }

    public T value(final String value) {
        getBuildingInstance().setValue(value);
        return getSelf();
    }

    public T constraint(final AllowedTimes allowedTimes) {
        getBuildingInstance().setConstraint(allowedTimes);
        return getSelf();
    }

    public T allowedValue(final String value) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedTimes());
        }
        getBuildingInstance().getConstraint().getValue().add(value);
        return getSelf();
    }

    public T allowedValues(final List<String> values) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedTimes());
        }
        getBuildingInstance().getConstraint().getValue().addAll(values);
        return getSelf();
    }

    public T allowedValues(final String... values) {
        return allowedValues(Arrays.asList(values));
    }

    public T interval(final List<String> interval) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedTimes());
        }
        getBuildingInstance().getConstraint().getInterval().add(interval);
        return getSelf();
    }

    public T interval(final String start, final String end) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedTimes());
        }
        getBuildingInstance().getConstraint().getInterval().add(Arrays.asList(start, end));
        return getSelf();
    }

    public T intervals(final List<List<String>> intervals) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedTimes());
        }
        getBuildingInstance().getConstraint().setInterval(intervals);
        return getSelf();
    }

    public T significantFigures(final int significantFigures) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedTimes());
        }
        getBuildingInstance().getConstraint().setSignificantFigures(significantFigures);
        return getSelf();
    }
}
