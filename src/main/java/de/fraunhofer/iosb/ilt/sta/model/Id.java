package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

/**
 * the interface that all Id implementations need to implement.
 *
 * @author scf
 */
public interface Id {

	public static Id tryToParse(String input) {
		if (input.startsWith("'")) {
			return new IdString(input.substring(1, input.length() - 1));
		}
		try {
			return new IdLong(Long.parseLong(input));
		} catch (NumberFormatException exc) {
			// not a long.
		}
		return new IdString(input);
	}

	/**
	 * Get the raw value of this Id.
	 *
	 * @return the raw value of this Id.
	 */
	@JsonValue
	public Object getValue();

	/**
	 * Get the value, formatted for use in a url.
	 *
	 * @return the value, formatted for use in a url.
	 */
	public String getUrl();

	/**
	 * Write the value to the given JsonGenerator.
	 *
	 * @param gen The JsonGenerator to write to.
	 * @throws IOException if the generator throws.
	 */
	public void writeTo(JsonGenerator gen) throws IOException;
}
