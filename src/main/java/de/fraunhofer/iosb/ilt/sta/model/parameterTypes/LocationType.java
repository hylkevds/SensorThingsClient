package de.fraunhofer.iosb.ilt.sta.model.parameterTypes;

public class LocationType extends TaskingParameterType {

    /**
     * See {@link TaskingParameterType#value} for information.
     */
    private final Double[] value;

    public LocationType (String label, String description, Double[] value) {
        this.label = label;
        this.description = description;
        this.value = value;
    }

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
    public boolean checkInput(Object input) {
        return value.equals(input);
    }
}
