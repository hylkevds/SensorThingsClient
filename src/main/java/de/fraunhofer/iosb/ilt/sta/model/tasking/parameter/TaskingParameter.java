package de.fraunhofer.iosb.ilt.sta.model.tasking.parameter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Michael Jacoby
 */
public interface TaskingParameter {
    @JsonProperty("type")
    public TaskingParameterType getType();
    
    @JsonProperty("name")
    public String getName();

    public void setName(String name);

    @JsonProperty("description")
    public String getDescription();

    public void setDescription(String description);

    @JsonProperty("label")
    public String getLabel();

    public void setLabel(String label);

}
