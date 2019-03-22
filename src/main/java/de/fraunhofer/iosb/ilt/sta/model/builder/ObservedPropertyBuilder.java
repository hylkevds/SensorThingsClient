package de.fraunhofer.iosb.ilt.sta.model.builder;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractObservedPropertyBuilder;
import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.ObservedProperty} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class ObservedPropertyBuilder extends AbstractObservedPropertyBuilder<ObservedPropertyBuilder> {
    private ObservedProperty observedProperty;

    public ObservedPropertyBuilder() {
        observedProperty = new ObservedProperty();
    }

    public static ObservedPropertyBuilder builder() {
        return new ObservedPropertyBuilder();
    }

    @Override
    public ObservedPropertyBuilder getSelf() {
        return this;
    }

    public void withName(String name) {
        observedProperty.setName(name);
    }

    public void withDescription(String description) {
        observedProperty.setDescription(description);
    }

    public void withDefinition(String definition) {
        observedProperty.setDefinition(definition);
    }

    public void withId(long id) {
        observedProperty.setId(new IdLong(id));
    }

    public void aDefaultObservedProperty() {
        observedProperty.setId(new IdLong(1l));
        observedProperty.setName("defaultObservedProperty");
        observedProperty.setDescription("defaultDescription");
        observedProperty.setDefinition("default.uri");
    }

    public ObservedProperty build() {
        ObservedProperty rObservedProperty = new ObservedProperty();
        rObservedProperty.setName(observedProperty.getName());
        rObservedProperty.setDefinition(observedProperty.getDefinition());
        rObservedProperty.setDescription(observedProperty.getDescription());
        rObservedProperty.setId(observedProperty.getId());
        return rObservedProperty;

    }
}
