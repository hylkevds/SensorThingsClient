package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.MultiDatastreamDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.geojson.Polygon;
import org.threeten.extra.Interval;

public class MultiDatastream extends Entity<MultiDatastream> {

	private String name;
	private String description;
	private String observationType;
	private List<String> multiObservationDataTypes;
	private List<UnitOfMeasurement> unitOfMeasurements;
	private Polygon observedArea;
	private Interval phenomenonTime;
	private Map<String, Object> properties;
	private Interval resultTime;

	@JsonProperty("Thing")
	private Thing thing;

	@JsonProperty("Sensor")
	private Sensor sensor;

	@JsonProperty("ObservedProperties")
	private EntityList<ObservedProperty> observedProperties = new EntityList<>(EntityType.OBSERVED_PROPERTIES);

	@JsonProperty("Observations")
	private EntityList<Observation> observations = new EntityList<>(EntityType.OBSERVATIONS);

	public MultiDatastream() {
		super(EntityType.MULTIDATASTREAM);
		unitOfMeasurements = new ArrayList<>();
		multiObservationDataTypes = new ArrayList<>();
		observationType = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_ComplexObservation";
	}

	public MultiDatastream(String name, String description, List<String> multiObservationDataTypes, List<UnitOfMeasurement> unitOfMeasurements) {
		this();
		this.name = name;
		this.description = description;
		this.observationType = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_ComplexObservation";
		this.multiObservationDataTypes = multiObservationDataTypes;
		this.unitOfMeasurements = unitOfMeasurements;
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
		final MultiDatastream other = (MultiDatastream) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.description, other.description)) {
			return false;
		}
		if (!Objects.equals(this.observationType, other.observationType)) {
			return false;
		}
		if (!Objects.equals(this.multiObservationDataTypes, other.multiObservationDataTypes)) {
			return false;
		}
		if (!Objects.equals(this.unitOfMeasurements, other.unitOfMeasurements)) {
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
		hash = 31 * hash + Objects.hashCode(this.name);
		hash = 31 * hash + Objects.hashCode(this.description);
		hash = 31 * hash + Objects.hashCode(this.observationType);
		hash = 31 * hash + Objects.hashCode(this.multiObservationDataTypes);
		hash = 31 * hash + Objects.hashCode(this.unitOfMeasurements);
		hash = 31 * hash + Objects.hashCode(this.properties);
		hash = 31 * hash + Objects.hashCode(this.resultTime);
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

	public List<String> getMultiObservationDataTypes() {
		return multiObservationDataTypes;
	}

	public void setMultiObservationDataTypes(List<String> multiObservationDataTypes) {
		this.multiObservationDataTypes = multiObservationDataTypes;
	}

	public void addMultiObservationDataTypes(String multiObservationDataType) {
		this.multiObservationDataTypes.add(multiObservationDataType);
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public List<UnitOfMeasurement> getUnitOfMeasurements() {
		return this.unitOfMeasurements;
	}

	public void setUnitOfMeasurements(List<UnitOfMeasurement> unitOfMeasurements) {
		this.unitOfMeasurements = unitOfMeasurements;
	}

	public void addUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) {
		this.unitOfMeasurements.add(unitOfMeasurement);
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

	public BaseDao<ObservedProperty> observedProperties() throws ServiceFailureException {
		return getService().observedProperties().setParent(this);
	}

	public EntityList<ObservedProperty> getObservedProperties() {
		return observedProperties;
	}

	public void setObservedProperties(EntityList<ObservedProperty> observedProperties) {
		this.observedProperties = observedProperties;
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
	public BaseDao<MultiDatastream> getDao(SensorThingsService service) {
		return new MultiDatastreamDao(service);
	}

	@Override
	public MultiDatastream withOnlyId() {
		MultiDatastream copy = new MultiDatastream();
		copy.setId(id);
		return copy;
	}

	@Override
	public String toString() {
		return super.toString() + " " + getName();
	}
}
