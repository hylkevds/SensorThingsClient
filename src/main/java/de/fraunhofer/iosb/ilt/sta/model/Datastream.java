package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.DatastreamDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import org.geojson.Polygon;
import org.threeten.extra.Interval;

public class Datastream extends Entity<Datastream> {

	private String name;
	private String description;
	private String observationType;
	// TODO: needs proper jackson deserialization
	private UnitOfMeasurement unitOfMeasurement;
	private Polygon observedArea;
	private Interval phenomenonTime;
	private Interval resultTime;

	@JsonProperty("Thing")
	private Thing thing;

	@JsonProperty("Sensor")
	private Sensor sensor;

	@JsonProperty("ObservedProperty")
	private ObservedProperty observedProperty;

	@JsonProperty("Observations")
	private EntityList<Observation> observations;

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

}
