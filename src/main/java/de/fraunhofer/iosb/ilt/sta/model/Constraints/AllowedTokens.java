package de.fraunhofer.iosb.ilt.sta.model.Constraints;

public class AllowedTokens extends Constraint {

	private String[] tokens;

	public AllowedTokens(String[] tokens) {
		this.tokens = tokens;
	}

	@Override
	public boolean isValid(Object input) {
		String inputString = (String) input;
		for (String token : tokens) {
			if (token.equals(inputString)) {
				return true;
			}
		}
		return false;
	}
}
