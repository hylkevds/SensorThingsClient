package de.fraunhofer.iosb.ilt.sta.model.parameterTypes;

import de.fraunhofer.iosb.ilt.sta.model.Constraints.AllowedInterval;
import de.fraunhofer.iosb.ilt.sta.model.Constraints.AllowedValues;
import de.fraunhofer.iosb.ilt.sta.model.Constraints.Constraint;

public class QuantityType extends TaskingParameterType {

    /**
     * See {@link TaskingParameterType#value} for information.
     */
    private Double value;
	private Constraint constraint;

    public QuantityType(String label, String description, Double value, AllowedValues constraint) {
        this.label = label;
        this.description = description;
        this.constraint = constraint;
        this.value = checkInput(value) ? value : null;
    }

	public QuantityType(String label, String description, Double value, AllowedInterval constraint) {
		this.label = label;
		this.description = description;
		this.constraint = constraint;
		this.value = checkInput(value) ? value : null;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
    public boolean checkInput(Object input) {
        return constraint.isValid(input);
    }
}
