package de.fraunhofer.iosb.ilt.sta.model.tasking.parameter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Jacoby
 */
public class TaskingParameterChoiceConstraint extends TaskingParameterConstraint {

    @JsonProperty("value")
    private List<String> choices;
    
    public TaskingParameterChoiceConstraint() {
        this.choices = new ArrayList<>();
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }
}
