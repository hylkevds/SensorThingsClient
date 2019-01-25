package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.dao.ObservedPropertyDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ObservedProperty extends Entity<ObservedProperty> {

    private String name;
    private String definition;
    private String description;
    private Map<String, Object> properties;

    private final EntityList<Datastream> datastreams = new EntityList<>(EntityType.DATASTREAMS);
    private final EntityList<MultiDatastream> multiDatastreams = new EntityList<>(EntityType.MULTIDATASTREAMS);

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
    protected void ensureServiceOnChildren(SensorThingsService service) {
        datastreams.setService(service, Datastream.class);
        multiDatastreams.setService(service, MultiDatastream.class);
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
        if (!Objects.equals(this.properties, other.properties)) {
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
        hash = 71 * hash + Objects.hashCode(this.properties);
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

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public BaseDao<Datastream> datastreams() {
        return getService().datastreams().setParent(this);
    }

    @JsonProperty("Datastreams")
    public EntityList<Datastream> getDatastreams() {
        return this.datastreams;
    }

    @JsonProperty("Datastreams")
    public void setDatastreams(List<Datastream> datastreams) {
        this.datastreams.replaceAll(datastreams);
    }

    public BaseDao<MultiDatastream> multiDatastreams() {
        return getService().multiDatastreams().setParent(this);
    }

    @JsonProperty("MultiDatastreams")
    public EntityList<MultiDatastream> getMultiDatastreams() {
        return this.multiDatastreams;
    }

    @JsonProperty("MultiDatastreams")
    public void setMultiDatastreams(List<MultiDatastream> multiDatastreams) {
        this.multiDatastreams.replaceAll(multiDatastreams);
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

    @Override
    public String toString() {
        return super.toString() + " " + getName();
    }
}
