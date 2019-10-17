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
import de.fraunhofer.iosb.ilt.swe.common.AbstractSWEIdentifiable;

/**
 * Base class for any {@link AbstractSWEIdentifiable} builder.
 * <p>
 * Any {@link AbstractSWEIdentifiableBuilder} is an {@link ExtensibleBuilder}.
 *
 * @param <T> the concrete {@link AbstractSWEIdentifiable} type to build
 * @param <U> the concrete type of this {@link AbstractSWEIdentifiableBuilder}
 * @author Michael Jacoby
 */
public abstract class AbstractSWEIdentifiableBuilder<T extends AbstractSWEIdentifiable, U extends AbstractSWEIdentifiableBuilder<T, U>> extends ExtensibleBuilder<T, U> {

    public U id(final String id) {
        getBuildingInstance().setIdentifier(id);
        return getSelf();
    }

    public U label(final String label) {
        getBuildingInstance().setLabel(label);
        return getSelf();
    }

    public U description(final String description) {
        getBuildingInstance().setDescription(description);
        return getSelf();
    }
}
