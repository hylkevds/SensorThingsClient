package de.fraunhofer.iosb.ilt.sta.model.tasking.parameter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Jacoby
 */
public abstract class AbstractConstrainedTaskingParameter extends AbstractTaskingParameter {

    private List<TaskingParameterConstraint> constraints;

    public AbstractConstrainedTaskingParameter() {
        this.constraints = new ArrayList<>();
    }
    
    public List<TaskingParameterConstraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<TaskingParameterConstraint> constraints) {
        this.constraints = constraints;
    }
        
    

}
