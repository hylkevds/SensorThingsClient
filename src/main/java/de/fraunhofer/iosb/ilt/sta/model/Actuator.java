/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fraunhofer.iosb.ilt.sta.dao.ActuatorDao;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Michael Jacoby
 */
public class Actuator extends Entity<Actuator> {

    private String name;
    private String description;
    private String encodingType;
    private Object metadata;
    private Map<String, Object> properties;

    private final EntityList<TaskingCapability> taskingCapabilities = new EntityList<>(EntityType.TASKING_CAPABILITIES);

    public Actuator() {
        super(EntityType.SENSOR);
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
        taskingCapabilities.setService(service, TaskingCapability.class);
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
        if (!Objects.equals(this.properties, other.properties)) {
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
        hash = 59 * hash + Objects.hashCode(this.properties);
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

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public BaseDao<TaskingCapability> taskingCapabilities() {
        return getService().taskingCapabilities().setParent(this);
    }

    @JsonProperty("TaskingCapabilities")
    public EntityList<TaskingCapability> getTaskingCapabilities() {
        return this.taskingCapabilities;
    }

    @JsonProperty("TaskingCapabilities")
    public void setTaskingCapabilities(List<TaskingCapability> taskingCapabilities) {
        this.taskingCapabilities.replaceAll(taskingCapabilities);
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
        return super.toString() + " " + getName();
    }

}
