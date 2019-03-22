package de.fraunhofer.iosb.ilt.sta.model.parameterTypes;

import java.time.LocalDateTime;

public class TimeType extends TaskingParameterType {

	/**
	 * See {@link TaskingParameterType#value} for information.
	 */
	private LocalDateTime value;

	public TimeType(String label, String description, LocalDateTime value) {
		this.label = label;
		this.description = description;
		this.value = checkInput(value) ? value : null;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public boolean checkInput(Object input) {
		return true;
		/*
		If a LocalDateTime object is provided to the constructor, it will always be correctly formatted.
		 */
	}
}
