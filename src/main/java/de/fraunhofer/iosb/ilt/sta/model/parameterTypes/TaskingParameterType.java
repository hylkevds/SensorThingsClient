package de.fraunhofer.iosb.ilt.sta.model.parameterTypes;


import de.fraunhofer.iosb.ilt.sta.model.Constraints.Constraint;

public abstract class TaskingParameterType {
    protected final String name = this.getClass().getName();
    protected String label;
    protected String description;
    protected Constraint constraint;

    /**
     * This attribute will only be set if the input in the constructor was valid. Validity is checked by the
     * corresponding constraint. If the input is invalid, the value will be null.
     */
    protected Object value;

	public abstract Object getValue();

	public String getLabel() {
		return label;
	}

	public abstract boolean checkInput(Object input);
}
