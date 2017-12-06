package de.fraunhofer.iosb.ilt.sta.model.ext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.MultiDatastream;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author scf
 */
@JsonIgnoreProperties("dataArray@iot.count")
public class DataArrayValue {

	/**
	 * The observation properties that can appear in a DataArray.
	 */
	public static enum Property {
		Id("id"),
		PhenomenonTime("phenomenonTime"),
		Result("result"),
		ResultTime("resultTime"),
		ResultQuality("resultQuality"),
		ValidTime("validTime"),
		Parameters("parameters"),
		FeatureOfInterest("FeatureOfInterest/id");
		public final String name;

		private Property(String name) {
			this.name = name;
		}

	}

	/**
	 * The set of properties used in a specific dataArray.
	 */
	public static class VisibleProperties {

		public final boolean id;
		public final boolean phenomenonTime;
		public final boolean result;
		public final boolean resultTime;
		public final boolean resultQuality;
		public final boolean validTime;
		public final boolean parameters;
		public final boolean featureOfInterest;

		public VisibleProperties() {
			this(false);
		}

		public VisibleProperties(boolean allValue) {
			id = allValue;
			phenomenonTime = allValue;
			result = allValue;
			resultTime = allValue;
			resultQuality = allValue;
			validTime = allValue;
			parameters = allValue;
			featureOfInterest = allValue;
		}

		public VisibleProperties(Set<Property> select) {
			id = select.contains(Property.Id);
			phenomenonTime = select.contains(Property.PhenomenonTime);
			result = select.contains(Property.Result);
			resultTime = select.contains(Property.ResultTime);
			resultQuality = select.contains(Property.ResultQuality);
			validTime = select.contains(Property.ValidTime);
			parameters = select.contains(Property.Parameters);
			featureOfInterest = select.contains(Property.FeatureOfInterest);
		}

		public List<String> getComponents() {
			List<String> components = new ArrayList<>();
			if (id) {
				components.add(Property.Id.name);
			}
			if (phenomenonTime) {
				components.add(Property.PhenomenonTime.name);
			}
			if (result) {
				components.add(Property.Result.name);
			}
			if (resultTime) {
				components.add(Property.ResultTime.name);
			}
			if (resultQuality) {
				components.add(Property.ResultQuality.name);
			}
			if (validTime) {
				components.add(Property.ValidTime.name);
			}
			if (parameters) {
				components.add(Property.Parameters.name);
			}
			if (featureOfInterest) {
				components.add(Property.FeatureOfInterest.name);
			}
			return components;
		}

		public List<Object> fromObservation(Observation o) {
			List<Object> value = new ArrayList<>();
			if (id) {
				value.add(o.getId());
			}
			if (phenomenonTime) {
				value.add(o.getPhenomenonTime());
			}
			if (result) {
				value.add(o.getResult());
			}
			if (resultTime) {
				value.add(o.getResultTime());
			}
			if (resultQuality) {
				value.add(o.getResultQuality());
			}
			if (validTime) {
				value.add(o.getValidTime());
			}
			if (parameters) {
				value.add(o.getParameters());
			}
			if (featureOfInterest) {
				try {
					value.add(o.getFeatureOfInterest().getId());
				} catch (ServiceFailureException ex) {
					value.add(null);
				}
			}
			return value;
		}
	}

	@JsonProperty("Datastream")
	private Datastream datastream;
	@JsonProperty("MultiDatastream")
	private MultiDatastream multiDatastream;
	@JsonIgnore
	private VisibleProperties visibleProperties;
	@JsonProperty("components")
	private List<String> components;
	@JsonProperty("dataArray")
	private List<List<Object>> dataArray = new ArrayList<>();
	@JsonIgnore
	private List<Observation> observations = new ArrayList<>();

	public DataArrayValue() {
		this.visibleProperties = new VisibleProperties(true);
		this.components = visibleProperties.getComponents();
	}

	public DataArrayValue(Datastream datastream, Set<Property> properties) {
		this.datastream = datastream.withOnlyId();
		this.visibleProperties = new VisibleProperties(properties);
		this.components = visibleProperties.getComponents();
	}

	public DataArrayValue(MultiDatastream multiDatastream, Set<Property> properties) {
		this.multiDatastream = multiDatastream.withOnlyId();
		this.visibleProperties = new VisibleProperties(properties);
		this.components = visibleProperties.getComponents();
	}

	public Datastream getDatastream() {
		return datastream;
	}

	public void setDatastream(Datastream datastream) {
		if (multiDatastream != null) {
			throw new IllegalArgumentException("Can not have both a Datastream and a MultiDatastream.");
		}
		this.datastream = datastream.withOnlyId();
	}

	public MultiDatastream getMultiDatastream() {
		return multiDatastream;
	}

	public void setMultiDatastream(MultiDatastream multiDatastream) {
		if (datastream != null) {
			throw new IllegalArgumentException("Can not have both a Datastream and a MultiDatastream.");
		}
		this.multiDatastream = multiDatastream.withOnlyId();
	}

	public List<String> getComponents() {
		return components;
	}

	/**
	 * Set the components to transmit. Throws IllegalStateException when
	 * observations have already be added.
	 *
	 * @param properties The components to set.
	 */
	public void setComponents(Set<Property> properties) {
		if (!dataArray.isEmpty()) {
			throw new IllegalStateException("Can not change components after adding Observations.");
		}
		visibleProperties = new VisibleProperties(properties);
		components = visibleProperties.getComponents();
	}

	/**
	 * Add an observation to this DataArray. The Datastream or MultiDatastream
	 * of the Observation is ignored.
	 *
	 * @param o The Observation to add.
	 */
	public void addObservation(Observation o) {
		dataArray.add(visibleProperties.fromObservation(o));
		observations.add(o);
	}

	public List<Observation> getObservations() {
		return observations;
	}

	public List<List<Object>> getDataArray() {
		return dataArray;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + Objects.hashCode(this.datastream);
		hash = 29 * hash + Objects.hashCode(this.multiDatastream);
		hash = 29 * hash + Objects.hashCode(this.components);
		hash = 29 * hash + Objects.hashCode(this.dataArray);
		return hash;
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
		final DataArrayValue other = (DataArrayValue) obj;
		if (!Objects.equals(this.datastream, other.datastream)) {
			return false;
		}
		if (!Objects.equals(this.multiDatastream, other.multiDatastream)) {
			return false;
		}
		if (!Objects.equals(this.components, other.components)) {
			return false;
		}
		return Objects.equals(this.dataArray, other.dataArray);
	}

	/**
	 * Helper for generating a key for a DataArray for a given Observation. The
	 * Observation must have a (Multi)Datastream that already exists in the
	 * server (has an id).
	 *
	 * @param observation the observation to generate a key for.
	 * @return a key to use for the DataArray for the Observation.
	 */
	public static String dataArrayKeyFor(Observation observation) {
		Datastream ds = null;
		try {
			ds = observation.getDatastream();
		} catch (ServiceFailureException ex) {
			// No Datastream.
		}
		if (ds == null) {
			MultiDatastream mds = null;
			try {
				mds = observation.getMultiDatastream();
			} catch (ServiceFailureException ex) {
				// No Mds.
			}
			if (mds == null) {
				throw new IllegalArgumentException("Observation must have a Datastream or MultiDatastream.");
			}
			return "mds-" + mds.getId().toString();
		}
		return "ds-" + ds.getId().toString();
	}
}
