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
import de.fraunhofer.iosb.ilt.swe.common.constraint.AllowedTokens;
import de.fraunhofer.iosb.ilt.swe.common.simple.Category;
import java.util.Arrays;
import java.util.List;

/**
 * /**
 * Base class for any {@link Category} builder.
 * <p>
 * Any {@link AbstractCategoryBuilder} is an {@link ExtensibleBuilder}.
 *
 * @param <T> the concrete {@link Category} type to build
 * @author Michael Jacoby
 */
public abstract class AbstractCategoryBuilder<T extends AbstractCategoryBuilder<T>> extends AbstractSimpleComponentBuilder<Category, T> {

    @Override
    protected Category newBuildingInstance() {
        return new Category();
    }

    public T value(final String value) {
        getBuildingInstance().setValue(value);
        return getSelf();
    }

    public T constraint(final AllowedTokens allowedTokens) {
        getBuildingInstance().setConstraint(allowedTokens);
        return getSelf();
    }

    public T allowedValue(final String value) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedTokens());
        }
        getBuildingInstance().getConstraint().getValue().add(value);
        return getSelf();
    }

    public T allowedValues(final List<String> values) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedTokens());
        }
        getBuildingInstance().getConstraint().getValue().addAll(values);
        return getSelf();
    }

    public T allowedValues(final String... values) {
        return allowedValues(Arrays.asList(values));
    }

    public T pattern(final String pattern) {
        if (getBuildingInstance().getConstraint() == null) {
            getBuildingInstance().setConstraint(new AllowedTokens());
        }
        getBuildingInstance().getConstraint().setPattern(pattern);
        return getSelf();
    }
}
