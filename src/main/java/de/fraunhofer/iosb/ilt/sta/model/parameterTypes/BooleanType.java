package de.fraunhofer.iosb.ilt.sta.model.parameterTypes;

import de.fraunhofer.iosb.ilt.sta.model.Constraints.AllowedTokens;

public class BooleanType extends TaskingParameterType {

	/**
	 * See {@link TaskingParameterType#value} for information.
	 */
	private Boolean value;
	private final AllowedTokens constraint = new AllowedTokens(new String[]{"true", "false"});

	public BooleanType(String label, String description, Boolean value) {
		this.label = label;
		this.description = description;
		this.value = checkInput(String.valueOf(value)) ? value : null;
	}

	@Override
	public Object getValue() {
		return this.value.toString();
	}

	@Override
	public boolean checkInput(Object input) {
		return constraint.isValid(input);
	}
}
