package de.fraunhofer.iosb.ilt.sta.model.tasking.parameter;

/**
 *
 * @author Michael Jacoby
 */
public class TextTaskingParameter extends AbstractConstrainedTaskingParameter {

    // CONSTRAINTS logic
    //
    // boolean      no constraints possible
    // numerical    list of allowed values and/or (un)bounded intervals
    // decimal      number if significant digits afer separator
    // category     list of choices
    // text         pattern
    @Override
    public TaskingParameterType getType() {
        return TaskingParameterType.TEXT;
    }
    
}
