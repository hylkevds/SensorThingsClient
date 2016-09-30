package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservationDao;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.time.ZonedDateTime;
import java.util.Map;
import org.threeten.extra.Interval;

public class Observation extends Entity<Observation> {

	// TODO: This can also be an Interval!
	private ZonedDateTime phenomenonTime;
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

	public ZonedDateTime getPhenomenonTime() {
		return this.phenomenonTime;
	}

	public void setPhenomenonTime(ZonedDateTime phenomenonTime) {
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
		if (datastream == null && service != null) {
			datastream = service.datastreams().find(this);
		}
		return datastream;
	}

	public void setDatastream(Datastream datastream) {
		this.datastream = datastream;
	}

	public FeatureOfInterest getFeatureOfInterest() throws ServiceFailureException {
		if (featureOfInterest == null && service != null) {
			featureOfInterest = service.featuresOfInterest().find(this);
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

}
