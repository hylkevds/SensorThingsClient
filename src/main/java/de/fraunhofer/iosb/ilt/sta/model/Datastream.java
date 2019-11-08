package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.DatastreamDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservationDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.geojson.GeoJsonObject;
import org.threeten.extra.Interval;

public class Datastream extends Entity<Datastream> {

    private String name;
    private String description;
    private String observationType;
    private UnitOfMeasurement unitOfMeasurement;
    private GeoJsonObject observedArea;
    private Interval phenomenonTime;
    private Map<String, Object> properties;
    private Interval resultTime;

    @JsonProperty("Thing")
    private Thing thing;

    @JsonProperty("Sensor")
    private Sensor sensor;

    @JsonProperty("ObservedProperty")
    private ObservedProperty observedProperty;

    private final EntityList<Observation> observations = new EntityList<>(EntityType.OBSERVATIONS);

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
    protected void ensureServiceOnChildren(SensorThingsService service) {
        if (thing != null) {
            thing.setService(service);
        }
        if (sensor != null) {
            sensor.setService(service);
        }
        if (observedProperty != null) {
            observedProperty.setService(service);
        }
        if (observations != null) {
            observations.setService(service, Observation.class);
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
        final Datastream other = (Datastream) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.description, other.description)
                && Objects.equals(this.observationType, other.observationType)
                && Objects.equals(this.unitOfMeasurement, other.unitOfMeasurement)
                && Objects.equals(this.properties, other.properties)
                && Objects.equals(this.resultTime, other.resultTime)
                && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                name,
                description,
                observationType,
                unitOfMeasurement,
                properties,
                resultTime);
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

    public GeoJsonObject getObservedArea() {
        return this.observedArea;
    }

    public void setObservedArea(GeoJsonObject observedArea) {
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

    public ObservationDao observations() {
        ObservationDao result = getService().observations();
        result.setParent(this);
        return result;
    }

    @JsonProperty("Observations")
    public EntityList<Observation> getObservations() {
        return this.observations;
    }

    @JsonProperty("Observations")
    public void setObservations(List<Observation> observations) {
        this.observations.replaceAll(observations);
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
