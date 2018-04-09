package de.fraunhofer.iosb.ilt.sta.model.builder.api;

/**
 * Base class for any {@link Builder}.
 * <p>
 * Any {@link AbstractBuilder} keeps a reference to the instance under construction.
 * This reference is created thanks to {@link #newBuildingInstance()} and finally returned when calling {@link #build()}
 *
 * @param <T> the instance class type to build
 * @author Aurelien Bourdon
 */
public abstract class AbstractBuilder<T> implements Builder<T> {

    private final T buildingInstance;

    protected AbstractBuilder() {
        buildingInstance = newBuildingInstance();
    }

    /**
     * Create the new instance that will be build by this {@link AbstractBuilder}
     *
     * @return the new instance that will be build by this {@link AbstractBuilder}
     */
    protected abstract T newBuildingInstance();

    /**
     * Get the instance under construction
     *
     * @return the instance under construction
     */
    protected T getBuildingInstance() {
        return buildingInstance;
    }

    /**
     * Finalize the build of the instance under construction and get it
     *
     * @return the created instance by this {@link AbstractBuilder}
     */
    @Override
    public T build() {
        return buildingInstance;
    }

}
