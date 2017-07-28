package de.fraunhofer.iosb.ilt.sta;

/**
 *
 * @author scf
 */
public class Utils {

	/**
	 * Replaces all ' in the string with ''.
	 *
	 * @param in The string to escape.
	 * @return The escaped string.
	 */
	public static String escapeForStringConstant(String in) {
		return in.replaceAll("'", "''");
	}
}
