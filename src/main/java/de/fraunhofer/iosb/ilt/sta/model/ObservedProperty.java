package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservedPropertyDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.net.URI;
import java.util.Objects;

public class ObservedProperty extends Entity<ObservedProperty> {

	private String name;
	private String definition;
	private String description;

	@JsonProperty("Datastreams")
	private EntityList<Datastream> datastreams = new EntityList<>(EntityType.DATASTREAMS);

	@JsonProperty("MultiDatastreams")
	private EntityList<MultiDatastream> multiDatastreams = new EntityList<>(EntityType.MULTIDATASTREAMS);

	public ObservedProperty() {
		super(EntityType.OBSERVED_PROPERTY);
	}

	public ObservedProperty(String name, URI definition, String description) {
		this();
		this.name = name;
		this.definition = definition.toASCIIString();
		this.description = description;
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
		final ObservedProperty other = (ObservedProperty) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.description, other.description)) {
			return false;
		}
		return Objects.equals(this.definition, other.definition);
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 71 * hash + Objects.hashCode(this.name);
		hash = 71 * hash + Objects.hashCode(this.definition);
		hash = 71 * hash + Objects.hashCode(this.description);
		return hash;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefinition() {
		return this.definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BaseDao<Datastream> datastreams() {
		return getService().datastreams().setParent(this);
	}

	public EntityList<Datastream> getDatastreams() {
		return this.datastreams;
	}

	public void setDatastreams(EntityList<Datastream> datastreams) {
		this.datastreams = datastreams;
	}

	public BaseDao<MultiDatastream> multiDatastreams() {
		return getService().multiDatastreams().setParent(this);
	}

	public EntityList<MultiDatastream> getMultiDatastreams() {
		return this.multiDatastreams;
	}

	public void setMultiDatastreams(EntityList<MultiDatastream> multiDatastreams) {
		this.multiDatastreams = multiDatastreams;
	}

	@Override
	public BaseDao<ObservedProperty> getDao(SensorThingsService service) {
		return new ObservedPropertyDao(service);
	}

	@Override
	public ObservedProperty withOnlyId() {
		ObservedProperty copy = new ObservedProperty();
		copy.setId(id);
		return copy;
	}
}
