package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Entity;

/**
 * Base type for any {@link Builder} of {@link Entity}
 *
 * @param <T> the {@link Entity} class type to build
 * @author Aurelien Bourdon
 */
public interface EntityBuilder<T extends Entity<T>> extends Builder<T> {
}
