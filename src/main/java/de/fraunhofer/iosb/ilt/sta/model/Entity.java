package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.net.URI;

/**
 * An abstract representation of an entity.
 *
 * @author Nils Sommer, Hylke van der Schaaf
 * @param <T> The type of the entity implementing this interface
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Entity<T extends Entity> {

	@JsonProperty("@iot.id")
	protected Long id;

	@JsonProperty("@iot.selfLink")
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
}
