package de.fraunhofer.iosb.ilt.sta.model.tasking.parameter;

/**
 *
 * @author Michael Jacoby
 */
public abstract class AbstractTaskingParameter implements TaskingParameter {

    protected String name;
    protected String description;
    protected String label;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

}
