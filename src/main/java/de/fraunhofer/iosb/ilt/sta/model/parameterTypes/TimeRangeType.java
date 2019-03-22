package de.fraunhofer.iosb.ilt.sta.model.parameterTypes;

import de.fraunhofer.iosb.ilt.sta.model.Constraints.AllowedTimeRange;

import java.time.LocalDateTime;

public class TimeRangeType extends TaskingParameterType {

	/**
	 * See {@link TaskingParameterType#value} for information.
	 */
	private LocalDateTime[] value;
	private AllowedTimeRange constraint;

	public TimeRangeType(String label, String description, LocalDateTime[] value, AllowedTimeRange constraint) {
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
