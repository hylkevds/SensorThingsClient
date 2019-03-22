package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.ActuatorDao;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

import java.util.Objects;

public class Actuator extends Entity<Actuator>{

    private String name;
    private String description;
    private String encodingType;
    private Object metadata;

    @JsonProperty("TaskingCapability")
    private EntityList<TaskingCapability> capabilities = new EntityList<>(EntityType.TASKING_CAPABILITIES);

    @JsonProperty(value = "id")
    private Id number;

    public Actuator() {
        super(EntityType.ACTUATOR);
    }

    public Actuator(String name, String description, String encodingType, Object metadata) {
        this();
        this.name = name;
        this.description = description;
        this.encodingType = encodingType;
        this.metadata = metadata;
    }


    @Override
    protected void ensureServiceOnChildren(SensorThingsService service) {
        capabilities.setService(service, TaskingCapability.class);
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
        final Actuator other = (Actuator) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.encodingType, other.encodingType)) {
            return false;
        }
        if (!Objects.equals(this.metadata, other.metadata)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + Objects.hashCode(this.description);
        hash = 59 * hash + Objects.hashCode(this.encodingType);
        hash = 59 * hash + Objects.hashCode(this.metadata);
        return hash;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public EntityList<TaskingCapability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(EntityList<TaskingCapability> capabilities) {
        this.capabilities = capabilities;
    }

    public BaseDao<TaskingCapability> taskingCapabilities() {
        return getService().taskingCapabilities().setParent(this);
    }

    public Id getNumber() {
        return number;
    }

    public void setNumber(Id number) {
        this.number = number;
        this.id = number;
    }

    @Override
    public BaseDao<Actuator> getDao(SensorThingsService service) {
        return new ActuatorDao(service);
    }

    @Override
    public Actuator withOnlyId() {
        Actuator copy = new Actuator();
        copy.setId(id);
        return copy;
    }

    @Override
    public String toString() {
        return (super.toString() + " " + this.name);
    }
}

