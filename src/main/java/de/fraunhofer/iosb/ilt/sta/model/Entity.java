package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.net.URI;
import java.util.Objects;

/**
 * An abstract representation of an entity. Entities are considered equal when
 * all entity properties (non-navigation properties) are equal.
 *
 * @author Nils Sommer, Hylke van der Schaaf
 * @param <T> The type of the entity implementing this interface
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Entity<T extends Entity<T>> {

	@JsonProperty(value = "@iot.id")
	protected Long id;

	@JsonProperty(value = "@iot.selfLink", access = JsonProperty.Access.READ_ONLY)
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

	public Entity(EntityType type, long id) {
		this.type = type;
		this.id = id;
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

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public URI getSelfLink() {
		return this.selfLink;
	}

	public void setSelfLink(URI selfLink) {
		this.selfLink = selfLink;
	}

	public void setService(SensorThingsService service) {
		this.service = service;
	}

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

}
