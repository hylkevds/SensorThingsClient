package de.fraunhofer.iosb.ilt.sta.model.Constraints;

public class AllowedValues extends Constraint {

	private double[] values;

	public AllowedValues(double[] values) {
		this.values = values;
	}

	@Override
	public boolean isValid(Object input) {
		double inputDouble = (double) input;
		for (double value : values) {
			if (value == inputDouble) {
				return true;
			}
		}
		return false;
	}
}
