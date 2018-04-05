package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Entity;

/**
 * Base class for any {@link EntityBuilder}.
 * <p>
 * Any {@link EntityBuilder} is an {@link ExtensibleBuilder}.
 *
 * @param <T> the instance class type to build
 * @param <U> the type of the concrete class that extends this {@link AbstractEntityBuilder}
 * @author Aurelien Bourdon
 */
public abstract class AbstractEntityBuilder<T extends Entity<T>, U extends AbstractEntityBuilder<T, U>> extends ExtensibleBuilder<T, U> implements EntityBuilder<T> {
}
