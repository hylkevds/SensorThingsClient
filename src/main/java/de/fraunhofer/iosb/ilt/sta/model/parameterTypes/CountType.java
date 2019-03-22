package de.fraunhofer.iosb.ilt.sta.model.parameterTypes;

import de.fraunhofer.iosb.ilt.sta.model.Constraints.AllowedInterval;

public class CountType extends TaskingParameterType {

	/**
	 * See {@link TaskingParameterType#value} for information.
	 */
	private Double value;
	private final AllowedInterval constraint;

	public CountType(String label, String description, Double value, AllowedInterval constraint) {
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
