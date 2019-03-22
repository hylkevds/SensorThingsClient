package de.fraunhofer.iosb.ilt.sta.model.parameterTypes;

import de.fraunhofer.iosb.ilt.sta.model.Constraints.AllowedTokens;

public class CategoryType extends TaskingParameterType {

	/**
	 * See {@link TaskingParameterType#value} for information.
	 */
	private String value;
	private final AllowedTokens constraint;

	public CategoryType(String label, String description, String value, AllowedTokens constraint) {
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
