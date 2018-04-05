package de.fraunhofer.iosb.ilt.sta.model.builder.api;

/**
 * Base class for any {@link Builder} that allows to be inherited to change its behaviour.
 * <p>
 * Any {@link ExtensibleBuilder} keeps a reference to the instance under construction.
 * This reference is created thanks to {@link #newBuildingInstance()} and finally returned when calling {@link #build()}
 *
 * @param <T> the instance class type to build
 * @param <U> the type of the concrete class that extends this {@link ExtensibleBuilder}
 * @author Aurelien Bourdon
 */
public abstract class ExtensibleBuilder<T, U extends ExtensibleBuilder<T, U>> implements Builder<T> {

    private final T buildingInstance;

    protected ExtensibleBuilder() {
        buildingInstance = newBuildingInstance();
    }

    /**
     * Create the new instance that will be build by this {@link ExtensibleBuilder}
     *
     * @return the new instance that will be build by this {@link ExtensibleBuilder}
     */
    protected abstract T newBuildingInstance();

    /**
     * Get the reference to the concrete instance that implements this {@link ExtensibleBuilder}
     *
     * @return the reference to the concrete instance that implements this {@link ExtensibleBuilder}
     */
    protected abstract U getSelf();

    /**
     * Finalize the build of the instance under construction and get it
     *
     * @return the created instance by this {@link ExtensibleBuilder}
     */
    @Override
    public T build() {
        return buildingInstance;
    }

    /**
     * Get the instance under construction
     *
     * @return the instance under construction
     */
    protected T getBuildingInstance() {
        return buildingInstance;
    }

}
