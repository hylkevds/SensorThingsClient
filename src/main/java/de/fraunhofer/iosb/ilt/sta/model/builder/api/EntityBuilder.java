package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.Id;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * Base class for any {@link Entity} builder.
 * <p>
 * Any {@link EntityBuilder} is an {@link ExtensibleBuilder}.
 *
 * @param <T> the concrete {@link Entity} type to build
 * @param <U> the concrete type of this {@link EntityBuilder}
 * @author Aurelien Bourdon, Michael Jacoby
 */
public abstract class EntityBuilder<T extends Entity<T>, U extends EntityBuilder<T, U>> extends ExtensibleBuilder<T, U> {

    public U id(final Id id) {
        getBuildingInstance().setId(id);
        return getSelf();
    }

    public U service(final SensorThingsService service) {
        getBuildingInstance().setService(service);
        return getSelf();
    }

}
