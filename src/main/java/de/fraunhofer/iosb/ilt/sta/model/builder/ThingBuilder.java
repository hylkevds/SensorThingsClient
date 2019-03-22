package de.fraunhofer.iosb.ilt.sta.model.builder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.fraunhofer.iosb.ilt.sta.model.builder.api.AbstractThingBuilder;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.model.*;


/**
 * Default
 * {@link de.fraunhofer.iosb.ilt.sta.model.Thing} {@link de.fraunhofer.iosb.ilt.sta.model.builder.api.Builder}
 *
 * @author Aurelien Bourdon
 */
public final class ThingBuilder extends AbstractThingBuilder<ThingBuilder> {
    private Thing thing;

    public ThingBuilder() {
        thing = new Thing();
    }

    public static ThingBuilder builder() {
        return new ThingBuilder();
    }

    @Override
    public ThingBuilder getSelf() {
        return this;
    }


    public void withName(String name) {
        thing.setName(name);
    }

    public void withDescription(String description) {
        thing.setDescription(description);
    }

    public void withProperies(Map<String, Object> properies) {
        thing.setProperties(properies);
    }

    public void withLocation(Location location) {
        EntityList<Location> locations = new EntityList<>(EntityType.LOCATIONS);
        locations.add(location);
        thing.setLocations(locations);
    }

    public void aDefaultThing() throws IOException {
        thing.setName("defaultThing");
        thing.setDescription("defaultDescription");
        Map<String, Object> dmap = new HashMap<>();
        dmap.put("defaulProperty", "defaultValue");
        thing.setProperties(dmap);
        LocationBuilder locationBuilder = new LocationBuilder();
        locationBuilder.aDefaultLocation();
        EntityList<Location> locations = new EntityList<>(EntityType.LOCATIONS);
        locations.add(locationBuilder.build());
        thing.setLocations(locations);
        thing.setId(new IdLong(1l));
    }

    public void withId(long id) {
        thing.setId(new IdLong(id));
    }

    public Thing build() {
        Thing rThing = new Thing(thing.getName(), thing.getDescription(), thing.getProperties());
        rThing.setLocations(thing.getLocations());
        rThing.setId(thing.getId());
        return rThing;
    }
}
