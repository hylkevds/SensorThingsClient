package de.fraunhofer.iosb.ilt.sta.model.Constraints;

public class AllowedTokenRange extends Constraint {

	private String lowerToken;
	private String higherToken;

	public AllowedTokenRange(String lowerToken, String higherToken) {
		this.lowerToken = lowerToken;
		this.higherToken = higherToken;
	}

	@Override
	public boolean isValid(Object input) {
		String[] tokens = (String[]) input;
		if (tokens.length == 2) {
			return (tokens[0].compareToIgnoreCase(lowerToken) >= 0) && (tokens[1].compareToIgnoreCase(higherToken) <= 0)
					&& (tokens[0].compareToIgnoreCase(tokens[1]) <= 0);
		}
		return false;
	}
}
