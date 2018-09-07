package de.fraunhofer.iosb.ilt.sta.model.ext;

import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import java.util.Collection;
import java.util.List;

/**
 * Methods for entity collections on top of the standard set of collection
 * methods.
 *
 * @author Nils Sommer
 *
 * @param <T> the entity's type
 */
public interface EntityCollection<T extends Entity> extends Collection<T> {

    public EntityType getType();

    /**
     * Convert the EntityCollection to a {@link List}.
     *
     * @return the list
     */
    List<T> toList();

    /**
     *
     * @return true if there is a nextLink to fetch more Entities.
     */
    boolean hasNextLink();

    /**
     * Use the nextLink to fetch more Entities.
     */
    void fetchNext();

    /**
     * Get the count value if available.
     *
     * @return the count value
     */
    long getCount();

    /**
     * Set the count value.
     *
     * @param count the count value
     */
    void setCount(long count);
}
