package de.fraunhofer.iosb.ilt.sta.model.tasking.parameter;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author Michael Jacoby
 */
public enum TaskingParameterType {
    BOOLEAN("Boolean"),
    CATEGORY("Category"),
    COUNT("Count"), // allowedValue, list of inclusive intervals and/or single values (all integer)
    QUANTITY("Quantity"), // uom, value: real, constraints: allowedValues: list of inclusive intervals and/or single values, number of digits
    TEXT("Text");
    //TIME // 

    private final String value;

    TaskingParameterType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
