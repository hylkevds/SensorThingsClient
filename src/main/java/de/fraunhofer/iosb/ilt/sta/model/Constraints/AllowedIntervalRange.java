package de.fraunhofer.iosb.ilt.sta.model.Constraints;

public class AllowedIntervalRange extends Constraint {

	private double min;
	private double max;

	public AllowedIntervalRange(double min, double max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean isValid(Object input) {
		double[] values = (double[]) input;
		if (values.length == 2) {
			return values[0] >= min && values[1] <= max && values[0] <= values[1];
		}
		return false;
	}
}
