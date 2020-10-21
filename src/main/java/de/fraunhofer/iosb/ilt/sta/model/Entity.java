package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.MqttException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.MqttSubscription;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jodah.typetools.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract representation of an entity. Entities are considered equal when
 * all entity properties (non-navigation properties) are equal.
 *
 * @author Nils Sommer, Hylke van der Schaaf, MIchael Jacoby
 * @param <T> The type of the entity implementing this interface
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Entity<T extends Entity<T>> {

    public static final String IOT_COUNT = "iot.count";
    public static final String AT_IOT_COUNT = "@iot.count";
    public static final String IOT_NAVIGATION_LINK = "iot.navigationLink";
    public static final String AT_IOT_NAVIGATION_LINK = "@iot.navigationLink";
    public static final String IOT_NEXT_LINK = "iot.nextLink";
    public static final String AT_IOT_NEXT_LINK = "@iot.nextLink";
    public static final String IOT_SELF_LINK = "iot.selfLink";
    public static final String AT_IOT_SELF_LINK = "@iot.selfLink";

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Entity.class);

    @JsonProperty(value = "@iot.id")
    protected Id id;

    @JsonIgnore
    protected URI selfLink;

    /**
     * The entity type.
     */
    @JsonIgnore
    private final EntityType type;
    /**
     * The service this thing belong to.
     */
    @JsonIgnore
    private SensorThingsService service;

    public Entity(EntityType type) {
        this.type = type;
    }

    public Entity(EntityType type, Id id) {
        this.type = type;
        this.id = id;
    }

    @JsonAnySetter
    public void handleNamespacedProperties(String name, Object value) {
        String[] split = name.split("@", 2);
        if (split.length < 2) {
            LOGGER.info("Ignoring unknown property {}.", name);
            return;
        }
        if (IOT_SELF_LINK.equals(split[1])) {
            setSelfLink(value.toString());
            return;
        }
        EntityType entityType = EntityType.byName(split[0]);
        if (entityType == null) {
            LOGGER.info("Unknown entity type '{}' for property '{}'", entityType, name);
            return;
        }
        try {
            Method method = getClass().getMethod("get" + entityType.getName(), (Class<?>[]) null);
            Object linkedEntity = method.invoke(this, (Object[]) null);
            if (linkedEntity instanceof EntityList) {
                EntityList entityList = (EntityList) linkedEntity;
                switch (split[1]) {
                    case IOT_COUNT:
                        if (value instanceof Number) {
                            entityList.setCount(((Number) value).longValue());
                        } else {
                            LOGGER.error("{} should have numeric value, not {}", name, value);
                        }
                        break;

                    case IOT_NEXT_LINK:
                        entityList.setNextLink(URI.create(value.toString()));
                        break;

                    case IOT_NAVIGATION_LINK:
                        // ignored
                        break;
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.info("Unknown entity type '{}' for property '{}'", entityType, name);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entity<?> other = (Entity<?>) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return this.type == other.type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.id);
        hash = 13 * hash + Objects.hashCode(this.type);
        return hash;
    }

    public EntityType getType() {
        return type;
    }

    public Id getId() {
        return this.id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    @JsonIgnore
    public URI getSelfLink() {
        return this.selfLink;
    }

    @JsonProperty
    public void setSelfLink(String selfLink) {
        try {
            this.selfLink = URI.create(selfLink);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid selflink: {}", selfLink);
        }
    }

    public void setSelfLink(URI selfLink) {
        this.selfLink = selfLink;
    }

    public void setService(SensorThingsService service) {
        if (this.service != service) {
            this.service = service;
            ensureServiceOnChildren(service);
        }
    }

    protected abstract void ensureServiceOnChildren(SensorThingsService service);

    public SensorThingsService getService() {
        return service;
    }

    public abstract BaseDao<T> getDao(SensorThingsService service);

    /**
     * Creates a copy of the entity, with only the ID field set. Useful when
     * creating a new entity that links to this entity.
     *
     * @return a copy with only the ID field set.
     */
    public abstract T withOnlyId();

    @Override
    public String toString() {
        if (id == null) {
            return "no id";
        }
        return id.toString();
    }

    public MqttSubscription subscribe(Consumer<T> handler) throws MqttException {
        return getDao(getService()).subscribe(this, handler);
    }

    public <U> MqttSubscription subscribeRelative(Consumer<U> handler, EntityType... path) throws MqttException {
        if (path != null && path.length > 0) {
            EntityType current = type;
            for (EntityType entityType : path) {
                if (!current.hasRelationTo(entityType)) {
                    throw new MqttException("invalid relative subscription path. EntityType " + current.getName() + " has no relation to " + entityType.getName());
                }
                current = entityType;
            }
        }
        EntityType resultType = getType();
        Class<?> handlerType = TypeResolver.resolveRawArgument(Consumer.class, handler.getClass());
        String topic = getService().getVersion().getUrlPattern() + "/" + getType().getPlural().getName() + "(" + getId() + ")/";
        if (path != null && path.length > 0) {
            topic += Stream.of(path).map(x -> x.getName()).collect(Collectors.joining("/"));
            resultType = path[path.length - 1];
        }
        if (!handlerType.isAssignableFrom(resultType.getType())) {
            throw new MqttException(String.format("Could not subscribe. Reason: type of provided message handler (%s) does not match expected result type (%s)",
                    handlerType,
                    resultType.getType()));
        }
        return getService().subscribe(topic, handler, (Class<U>) resultType.getType(), null);
    }

}
