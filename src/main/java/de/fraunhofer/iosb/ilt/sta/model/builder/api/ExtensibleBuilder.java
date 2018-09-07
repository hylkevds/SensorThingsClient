package de.fraunhofer.iosb.ilt.sta.model.builder.api;

/**
 * Base class for any {@link Builder} that allows to be inherited to change its
 * behaviour.
 *
 * @param <T> the instance class type to build
 * @param <U> the concrete type that extends this {@link ExtensibleBuilder}
 * @author Aurelien Bourdon
 */
public abstract class ExtensibleBuilder<T, U extends ExtensibleBuilder<T, U>> extends AbstractBuilder<T> {

    /**
     * Get the reference to the concrete instance that extends this
     * {@link ExtensibleBuilder}. Commonly, {@code this}.
     *
     * @return the reference to the concrete instance that extends this
     * {@link ExtensibleBuilder}. Commonly, {@code this}.
     */
    protected abstract U getSelf();

}
