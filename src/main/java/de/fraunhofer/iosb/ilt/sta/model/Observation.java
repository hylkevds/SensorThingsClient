package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservationDao;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import org.threeten.extra.Interval;

public class Observation extends Entity<Observation> {

	private TimeObject phenomenonTime;
	private Object result;
	private ZonedDateTime resultTime;
	private Object resultQuality; //DQ_Element
	private Interval validTime;
	private Map<String, Object> parameters;

	@JsonProperty("Datastream")
	private Datastream datastream;

	@JsonProperty("FeatureOfInterest")
	private FeatureOfInterest featureOfInterest;

	public Observation() {
		super(EntityType.OBSERVATION);
	}

	public Observation(Object result, Datastream datastream) {
		this();
		this.result = result;
		this.datastream = datastream;
	}

	public Observation(Object result, ZonedDateTime phenomenonTime) {
		this();
		this.result = result;
		this.phenomenonTime = new TimeObject(phenomenonTime);
	}

	public Observation(Object result, Interval phenomenonTime) {
		this();
		this.result = result;
		this.phenomenonTime = new TimeObject(phenomenonTime);
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
		final Observation other = (Observation) obj;
		if (!Objects.equals(this.phenomenonTime, other.phenomenonTime)) {
			return false;
		}
		if (!Objects.equals(this.result, other.result)) {
			return false;
		}
		if (!Objects.equals(this.resultTime, other.resultTime)) {
			return false;
		}
		if (!Objects.equals(this.resultQuality, other.resultQuality)) {
			return false;
		}
		if (!Objects.equals(this.validTime, other.validTime)) {
			return false;
		}
		if (!Objects.equals(this.parameters, other.parameters)) {
			return false;
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 89 * hash + Objects.hashCode(this.phenomenonTime);
		hash = 89 * hash + Objects.hashCode(this.result);
		hash = 89 * hash + Objects.hashCode(this.resultTime);
		hash = 89 * hash + Objects.hashCode(this.resultQuality);
		hash = 89 * hash + Objects.hashCode(this.validTime);
		hash = 89 * hash + Objects.hashCode(this.parameters);
		return hash;
	}

	public TimeObject getPhenomenonTime() {
		return this.phenomenonTime;
	}

	@JsonIgnore
	public void setPhenomenonTimeFrom(ZonedDateTime phenomenonTime) {
		this.phenomenonTime = new TimeObject(phenomenonTime);
	}

	@JsonIgnore
	public void setPhenomenonTimeFrom(Interval phenomenonTime) {
		this.phenomenonTime = new TimeObject(phenomenonTime);
	}

	public void setPhenomenonTime(TimeObject phenomenonTime) {
		this.phenomenonTime = phenomenonTime;
	}

	public Object getResult() {
		return this.result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public ZonedDateTime getResultTime() {
		return this.resultTime;
	}

	public void setResultTime(ZonedDateTime resultTime) {
		this.resultTime = resultTime;
	}

	public Object getResultQuality() {
		return this.resultQuality;
	}

	public void setResultQuality(Object resultQuality) {
		this.resultQuality = resultQuality;
	}

	public Interval getValidTime() {
		return this.validTime;
	}

	public void setValidTime(Interval validTime) {
		this.validTime = validTime;
	}

	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Datastream getDatastream() throws ServiceFailureException {
		if (datastream == null && getService() != null) {
			datastream = getService().datastreams().find(this);
		}
		return datastream;
	}

	public void setDatastream(Datastream datastream) {
		this.datastream = datastream;
	}

	public FeatureOfInterest getFeatureOfInterest() throws ServiceFailureException {
		if (featureOfInterest == null && getService() != null) {
			featureOfInterest = getService().featuresOfInterest().find(this);
		}
		return this.featureOfInterest;
	}

	public void setFeatureOfInterest(FeatureOfInterest featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
	}

	@Override
	public BaseDao<Observation> getDao(SensorThingsService service) {
		return new ObservationDao(service);
	}

	@Override
	public Observation withOnlyId() {
		Observation copy = new Observation();
		copy.setId(id);
		return copy;
	}

}
