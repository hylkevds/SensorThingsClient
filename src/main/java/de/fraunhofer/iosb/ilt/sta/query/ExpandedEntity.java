package de.fraunhofer.iosb.ilt.sta.query;

import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An expanded entity is an entity to be included into a request.
 *
 * @author Nils Sommer
 *
 */
public class ExpandedEntity {

    private final List<EntityType> entities;

    private ExpandedEntity(EntityType... entities) throws InvalidRelationException {
        this.entities = new ArrayList<>(entities.length);

        for (int i = 0; i < entities.length; i++) {
            if (i > 0) {
                if (!entities[i].hasRelationTo(entities[i - 1])) {
                    throw new InvalidRelationException(
                            String.format("%s is not directly related to %s",
                                    entities[i].getName(),
                                    entities[i - 1].getName()));
                }
            }
            this.entities.add(entities[i]);
        }
    }

    /**
     * Build an expanded entity. Entities can be nested to create an expanded
     * entity, e.g. Thing/Locations in a Datastream request will include the
     * Thing of the Datastream as well as all the Locations of the Thing.
     *
     * @param entities the entity types that construct this expanded entity
     * @return the expanded entity
     * @throws InvalidRelationException the entity types are not related to each
     * other
     */
    public static ExpandedEntity from(EntityType... entities) throws InvalidRelationException {
        return new ExpandedEntity(entities);
    }

    @Override
    public String toString() {
        return this.entities
                .stream()
                .map(e -> e.getName())
                .collect(Collectors.joining("/"));
    }

    public EntityType getDirectSibling() {
        return this.entities.get(0);
    }
}
