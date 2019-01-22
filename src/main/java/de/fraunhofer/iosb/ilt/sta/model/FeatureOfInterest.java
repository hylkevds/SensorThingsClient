package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.FeatureOfInterestDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.util.Map;
import java.util.Objects;
import org.geojson.GeoJsonObject;

public class FeatureOfInterest extends Entity<FeatureOfInterest> {

    private String name;
    private String description;
    private String encodingType;
    private GeoJsonObject feature;
    private Map<String, Object> properties;

    @JsonProperty("Observations")
    private final EntityList<Observation> observations = new EntityList<>(EntityType.OBSERVATIONS);

    public FeatureOfInterest() {
        super(EntityType.FEATURE_OF_INTEREST);
    }

    public FeatureOfInterest(String name, String description, String encodingType, GeoJsonObject feature) {
        this();
        this.name = name;
        this.description = description;
        this.encodingType = encodingType;
        this.feature = feature;
    }

    @Override
    protected void ensureServiceOnChildren(SensorThingsService service) {
        observations.setService(service, Observation.class);
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
        final FeatureOfInterest other = (FeatureOfInterest) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.encodingType, other.encodingType)) {
            return false;
        }
        if (!Objects.equals(this.feature, other.feature)) {
            return false;
        }
        if (!Objects.equals(this.properties, other.properties)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + Objects.hashCode(this.description);
        hash = 47 * hash + Objects.hashCode(this.encodingType);
        hash = 47 * hash + Objects.hashCode(this.feature);
        hash = 47 * hash + Objects.hashCode(this.properties);
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

    public String getEncodingType() {
        return this.encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public GeoJsonObject getFeature() {
        return this.feature;
    }

    public void setFeature(GeoJsonObject feature) {
        this.feature = feature;
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public BaseDao<Observation> observations() {
        return getService().observations().setParent(this);
    }

    public EntityList<Observation> getObservations() {
        return this.observations;
    }

    public void setObservations(EntityList<Observation> observations) {
        this.observations.replaceAll(observations);
    }

    @Override
    public BaseDao<FeatureOfInterest> getDao(SensorThingsService service) {
        return new FeatureOfInterestDao(service);
    }

    @Override
    public FeatureOfInterest withOnlyId() {
        FeatureOfInterest copy = new FeatureOfInterest();
        copy.setId(id);
        return copy;
    }

    @Override
    public String toString() {
        return super.toString() + " " + getName();
    }
}
