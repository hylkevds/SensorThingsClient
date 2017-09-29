package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.DatastreamDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.util.Map;
import java.util.Objects;
import org.geojson.Polygon;
import org.threeten.extra.Interval;

public class Datastream extends Entity<Datastream> {

	private String name;
	private String description;
	private String observationType;
	private UnitOfMeasurement unitOfMeasurement;
	private Polygon observedArea;
	private Interval phenomenonTime;
	private Map<String, Object> properties;
	private Interval resultTime;

	@JsonProperty("Thing")
	private Thing thing;

	@JsonProperty("Sensor")
	private Sensor sensor;

	@JsonProperty("ObservedProperty")
	private ObservedProperty observedProperty;

	@JsonProperty("Observations")
	private EntityList<Observation> observations = new EntityList<>(EntityType.OBSERVATIONS);

	public Datastream() {
		super(EntityType.DATASTREAM);
	}

	public Datastream(String name, String description, String observationType, UnitOfMeasurement unitOfMeasurement) {
		this();
		this.name = name;
		this.description = description;
		this.observationType = observationType;
		this.unitOfMeasurement = unitOfMeasurement;
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
		final Datastream other = (Datastream) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.description, other.description)) {
			return false;
		}
		if (!Objects.equals(this.observationType, other.observationType)) {
			return false;
		}
		if (!Objects.equals(this.unitOfMeasurement, other.unitOfMeasurement)) {
			return false;
		}
		if (!Objects.equals(this.properties, other.properties)) {
			return false;
		}
		if (!Objects.equals(this.resultTime, other.resultTime)) {
			return false;
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 17 * hash + Objects.hashCode(this.name);
		hash = 17 * hash + Objects.hashCode(this.description);
		hash = 17 * hash + Objects.hashCode(this.observationType);
		hash = 17 * hash + Objects.hashCode(this.unitOfMeasurement);
		hash = 17 * hash + Objects.hashCode(this.properties);
		hash = 17 * hash + Objects.hashCode(this.resultTime);
		return hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObservationType() {
		return this.observationType;
	}

	public void setObservationType(String observationType) {
		this.observationType = observationType;
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public UnitOfMeasurement getUnitOfMeasurement() {
		return this.unitOfMeasurement;
	}

	public void setUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public Polygon getObservedArea() {
		return this.observedArea;
	}

	public void setObservedArea(Polygon observedArea) {
		this.observedArea = observedArea;
	}

	public Interval getPhenomenonTime() {
		return this.phenomenonTime;
	}

	public void setPhenomenonTime(Interval phenomenonTime) {
		this.phenomenonTime = phenomenonTime;
	}

	public Interval getResultTime() {
		return this.resultTime;
	}

	public void setResultTime(Interval resultTime) {
		this.resultTime = resultTime;
	}

	public Thing getThing() throws ServiceFailureException {
		if (thing == null && getService() != null) {
			thing = getService().things().find(this);
		}
		return thing;
	}

	public void setThing(Thing thing) {
		this.thing = thing;
	}

	public Sensor getSensor() throws ServiceFailureException {
		if (sensor == null && getService() != null) {
			sensor = getService().sensors().find(this);
		}
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public ObservedProperty getObservedProperty() throws ServiceFailureException {
		if (observedProperty == null && getService() != null) {
			observedProperty = getService().observedProperties().find(this);
		}
		return observedProperty;
	}

	public void setObservedProperty(ObservedProperty observedProperty) {
		this.observedProperty = observedProperty;
	}

	public BaseDao<Observation> observations() {
		return getService().observations().setParent(this);
	}

	public EntityList<Observation> getObservations() {
		return this.observations;
	}

	public void setObservations(EntityList<Observation> observations) {
		this.observations = observations;
	}

	@Override
	public BaseDao<Datastream> getDao(SensorThingsService service) {
		return new DatastreamDao(service);
	}

	@Override
	public Datastream withOnlyId() {
		Datastream copy = new Datastream();
		copy.setId(id);
		return copy;
	}

	@Override
	public String toString() {
		return super.toString() + " " + getName();
	}
}
