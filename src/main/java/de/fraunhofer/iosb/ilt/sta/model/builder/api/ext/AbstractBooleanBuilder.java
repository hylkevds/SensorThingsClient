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
import de.fraunhofer.iosb.ilt.swe.common.simple.Count;
import de.fraunhofer.iosb.ilt.swe.common.simple.SweBoolean;

/**
 * /**
 * Base class for any {@link Count} builder.
 * <p>
 * Any {@link AbstractBooleanBuilder} is an {@link ExtensibleBuilder}.
 *
 * @param <T> the concrete {@link SweBoolean} type to build
 * @author Michael Jacoby
 */
public abstract class AbstractBooleanBuilder<T extends AbstractBooleanBuilder<T>> extends AbstractSimpleComponentBuilder<SweBoolean, T> {

    @Override
    protected SweBoolean newBuildingInstance() {
        return new SweBoolean();
    }

    public T value(final boolean value) {
        getBuildingInstance().setValue(value);
        return getSelf();
    }
}
