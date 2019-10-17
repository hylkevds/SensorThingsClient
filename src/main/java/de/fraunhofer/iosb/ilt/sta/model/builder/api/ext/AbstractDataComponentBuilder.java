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
import de.fraunhofer.iosb.ilt.swe.common.AbstractDataComponent;

/**
 * Base class for any {@link AbstractDataComponent} builder.
 * <p>
 * Any {@link AbstractDataComponentBuilder} is an {@link ExtensibleBuilder}.
 *
 * @param <T> the concrete {@link AbstractDataComponent} type to build
 * @param <U> the concrete type of this {@link AbstractDataComponentBuilder}
 * @author Michael Jacoby
 */
public abstract class AbstractDataComponentBuilder<T extends AbstractDataComponent, U extends AbstractDataComponentBuilder<T, U>> extends AbstractSWEIdentifiableBuilder<T, U> {

    public U name(final String name) {
        getBuildingInstance().setName(name);
        return getSelf();
    }

    public U definition(final String definition) {
        getBuildingInstance().setDefinition(definition);
        return getSelf();
    }

    public U optional(final boolean optional) {
        getBuildingInstance().setOptional(optional);
        return getSelf();
    }

    public U optional() {
        return optional(true);
    }

    public U updatable(final boolean updatable) {
        getBuildingInstance().setUpdatable(updatable);
        return getSelf();
    }

    public U updatable() {
        return updatable(true);
    }
}
