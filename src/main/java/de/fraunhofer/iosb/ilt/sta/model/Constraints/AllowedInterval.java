package de.fraunhofer.iosb.ilt.sta.model.Constraints;

public class AllowedInterval extends Constraint {

	private double min;
	private double max;

	public AllowedInterval(double min, double max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean isValid(Object input) {
		double inputDouble = (double) input;
		return inputDouble >= min && inputDouble <= max;
	}
}
