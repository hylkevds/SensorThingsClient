package de.fraunhofer.iosb.ilt.sta.dao;

import com.github.fge.jsonpatch.JsonPatchOperation;
import de.fraunhofer.iosb.ilt.sta.MqttException;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityProperty;
import de.fraunhofer.iosb.ilt.sta.model.Id;
import de.fraunhofer.iosb.ilt.sta.query.Expansion;
import de.fraunhofer.iosb.ilt.sta.query.Query;
import de.fraunhofer.iosb.ilt.sta.service.MqttSubscription;
import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * CRUD operations for data access objects (Daos).
 *
 * @author Nils Sommer, Hylke van der Schaaf, Michael Jacoby
 *
 * @param <T> the entity's type
 */
public interface Dao<T extends Entity<T>> {

    /**
     * Create a new entity.
     *
     * @param entity the entity to create
     * @throws ServiceFailureException the operation failed
     */
    void create(T entity) throws ServiceFailureException;

    /**
     * Find the entity related to the given parent, like the Thing for a
     * Datastream.
     *
     * @param parent The parent to find the singular entity for.
     * @return the singular entity linked from the parent.
     * @throws ServiceFailureException the operation failed
     */
    public T find(Entity<?> parent) throws ServiceFailureException;

    /**
     * Find an entity.
     *
     * @param id the entity's unique id
     * @return the entity
     * @throws ServiceFailureException the operation failed
     */
    T find(Id id) throws ServiceFailureException;

    /**
     * Find an entity.
     *
     * @param uri the entity's URI
     * @return the entity
     * @throws ServiceFailureException the operation failed
     */
    T find(URI uri) throws ServiceFailureException;

    /**
     * Find an entity including referenced entities from expansion.
     *
     * @param id the entity's unique id
     * @param expansion the expansion containing which referenced entities to
     * fetch
     * @return the entity
     * @throws ServiceFailureException the operation failed
     */
    T find(Id id, Expansion expansion) throws ServiceFailureException;

    /**
     * Update an entity.
     *
     * @param entity the entity to update
     * @throws ServiceFailureException the operation failed
     */
    void update(T entity) throws ServiceFailureException;

    /**
     * Update the given entity with the given patch. Does not update the entity
     * object itself. To see the result, fetch it anew from the server.
     *
     * @param entity The entity to update on the server.
     * @param patch The patch to apply to the entity.
     * @throws ServiceFailureException the operation failed
     */
    void patch(T entity, List<JsonPatchOperation> patch) throws ServiceFailureException;

    /**
     * Delete an entity.
     *
     * @param entity the entity to delete
     * @throws ServiceFailureException the operation failed
     */
    void delete(T entity) throws ServiceFailureException;

    /**
     * Start a query to find an entity collection.
     *
     * @return the query
     */
    Query<T> query();

    /**
     * Starts an MQTT subscription.
     *
     * @param handler a handler that is called upon received messages
     * @return object containing information about new MQTT subscription
     * @throws MqttException when subscription failed
     */
    MqttSubscription subscribe(Consumer<T> handler) throws MqttException;

    /**
     * Starts an MQTT subscription.
     *
     * @param filter predicate to further filter returned entities
     * @param handler a handler that is called upon received messages
     * @return object containing information about new MQTT subscription
     * @throws MqttException when subscription failed
     */
    MqttSubscription subscribe(Predicate<T> filter, Consumer<T> handler) throws MqttException;

    /**
     * Starts an MQTT subscription.
     *
     * @param handler a handler that is called upon received messages
     * @param properties properties to select, must be presten for entity type T
     * @return object containing information about new MQTT subscription
     * @throws MqttException when subscription failed
     */
    MqttSubscription subscribe(Consumer<T> handler, EntityProperty... properties) throws MqttException;

    /**
     * Starts an MQTT subscription.
     *
     * @param filter predicate to further filter returned entities
     * @param handler a handler that is called upon received messages
     * @param properties properties to select, must be presten for entity type T
     * @return object containing information about new MQTT subscription
     * @throws MqttException when subscription failed
     */
    MqttSubscription subscribe(Predicate<T> filter, Consumer<T> handler, EntityProperty... properties) throws MqttException;

    /**
     * Stops an MQTT subscription.
     *
     * @param subscription contains information on subscription
     * @throws MqttException when unsubscribing failed
     */
    void unsubscribe(MqttSubscription subscription) throws MqttException;

    /**
     * Stops all MQTT subscriptions.
     *
     * @throws MqttException when unsubscribing failed
     */
    void unsubscribe() throws MqttException;
}
