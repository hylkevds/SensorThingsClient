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
import de.fraunhofer.iosb.ilt.swe.common.simple.AbstractSimpleComponent;
import de.fraunhofer.iosb.ilt.swe.common.util.NillValue;
import java.util.List;

/**
 * Base class for any {@link AbstractSimpleComponent} builder.
 * <p>
 * Any {@link AbstractDataComponentBuilder} is an {@link ExtensibleBuilder}.
 *
 * @param <T> the concrete {@link AbstractSimpleComponent} type to build
 * @param <U> the concrete type of this {@link AbstractDataComponentBuilder}
 * @author Michael Jacoby
 */
public abstract class AbstractSimpleComponentBuilder<T extends AbstractSimpleComponent, U extends AbstractSimpleComponentBuilder<T, U>> extends AbstractDataComponentBuilder<T, U> {

    public U axisId(final String axisId) {
        getBuildingInstance().setAxisID(axisId);
        return getSelf();
    }

    public U nilValues(final List<NillValue> nilValues) {
        getBuildingInstance().setNilValues(nilValues);
        return getSelf();
    }
    
    public U nilValue(final NillValue nilValue) {
        getBuildingInstance().getNilValues().add(nilValue);
        return getSelf();
    }

    public U referenceFrame(final String referenceFrame) {
        getBuildingInstance().setReferenceFrame(referenceFrame);
        return getSelf();
    }
}
