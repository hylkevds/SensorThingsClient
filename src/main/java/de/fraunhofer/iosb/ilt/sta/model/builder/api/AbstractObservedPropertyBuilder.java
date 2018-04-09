package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.MultiDatastream;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;

import java.net.URI;
import java.util.List;

/**
 * Base class for any {@link EntityBuilder} of {@link ObservedProperty}
 *
 * @param <U> the type of the concrete class that extends this {@link AbstractObservedPropertyBuilder}
 * @author Aurelien Bourdon
 */
public abstract class AbstractObservedPropertyBuilder<U extends AbstractObservedPropertyBuilder<U>> extends EntityBuilder<ObservedProperty, U> {

    @Override
    protected ObservedProperty newBuildingInstance() {
        return new ObservedProperty();
    }

    public U name(final String name) {
        getBuildingInstance().setName(name);
        return getSelf();
    }

    public U definition(final URI definition) {
        getBuildingInstance().setDefinition(definition.toString());
        return getSelf();
    }

    public U description(final String description) {
        getBuildingInstance().setDescription(description);
        return getSelf();
    }

    public U datastreams(final List<Datastream> datastreams) {
        getBuildingInstance().getDatastreams().addAll(datastreams);
        return getSelf();
    }

    public U datastream(final Datastream datastream) {
        getBuildingInstance().getDatastreams().add(datastream);
        return getSelf();
    }

    public U multiDatastreams(final List<MultiDatastream> multiDatastreams) {
        getBuildingInstance().getMultiDatastreams().addAll(multiDatastreams);
        return getSelf();
    }

    public U multiDatastream(final MultiDatastream multiDatastream) {
        getBuildingInstance().getMultiDatastreams().add(multiDatastream);
        return getSelf();
    }

}
