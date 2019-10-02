package de.fraunhofer.iosb.ilt.sta.model.tasking.parameter;

/**
 *
 * @author Michael Jacoby
 */
public class TaskingParameterRegExConstraint extends TaskingParameterConstraint {

    private String pattern;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}
