package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.net.URI;

/**
 * An abstract representation of an entity.
 *
 * @author Nils Sommer
 * @param <T> The type of the entity implementing this interface
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Entity<T extends Entity> {

	@JsonProperty("@iot.id")
	protected Long id;

	@JsonProperty("@iot.selfLink")
	protected URI selfLink;

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

	public abstract BaseDao<T> getDao(SensorThingsService service);
}
