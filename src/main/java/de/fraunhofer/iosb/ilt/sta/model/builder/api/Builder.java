package de.fraunhofer.iosb.ilt.sta.model.builder.api;

/**
 * Base type for any builder
 *
 * @param <T> the class type to build
 * @author Aurelien Bourdon
 */
public interface Builder<T> {

    /**
     * Build the class instance
     *
     * @return a new class instance
     */
    T build();

}
