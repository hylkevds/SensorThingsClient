package de.fraunhofer.iosb.ilt.sta.model.parameterTypes;

import de.fraunhofer.iosb.ilt.sta.model.Constraints.AllowedIntervalRange;

public class CountRangeType extends TaskingParameterType {

	/**
	 * See {@link TaskingParameterType#value} for information.
	 */
	private double[] value;
	private final AllowedIntervalRange constraint;

	public CountRangeType(String label, String description, double[] value, AllowedIntervalRange constraint) {
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
