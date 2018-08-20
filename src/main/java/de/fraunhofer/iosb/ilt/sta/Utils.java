package de.fraunhofer.iosb.ilt.sta;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author scf
 */
public class Utils {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	/**
	 * Replaces all ' in the string with ''.
	 *
	 * @param in The string to escape.
	 * @return The escaped string.
	 */
	public static String escapeForStringConstant(String in) {
		return in.replaceAll("'", "''");
	}

	public static String quoteForUrl(Object in) {
		if (in instanceof Number) {
			return in.toString();
		}
		return "'" + escapeForStringConstant(in.toString()) + "'";
	}

	/**
	 * Urlencodes the given string, optionally not encoding forward slashes.
	 *
	 * In urls, forward slashes before the "?" must never be urlEncoded.
	 * Urlencoding of slashes could otherwise be used to obfuscate phising URLs.
	 *
	 * @param string The string to urlEncode.
	 * @param notSlashes If true, forward slashes are not encoded.
	 * @return The urlEncoded string.
	 */
	public static String urlEncode(String string, boolean notSlashes) {
		if (notSlashes) {
			return urlEncodeNotSlashes(string);
		}
		try {
			return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException ex) {
			LOGGER.error("Should not happen, UTF-8 should always be supported.", ex);
		}
		return string;
	}

	/**
	 * Urlencodes the given string
	 *
	 * @param string The string to urlEncode.
	 * @return The urlEncoded string.
	 */
	public static String urlEncode(String string) {
		try {
			return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException ex) {
			LOGGER.error("Should not happen, UTF-8 should always be supported.", ex);
		}
		return string;
	}

	/**
	 * Urlencodes the given string, except for the forward slashes.
	 *
	 * @param string The string to urlEncode.
	 * @return The urlEncoded string.
	 */
	public static String urlEncodeNotSlashes(String string) {
		try {
			String[] split = string.split("/");
			for (int i = 0; i < split.length; i++) {
				split[i] = URLEncoder.encode(split[i], StandardCharsets.UTF_8.name());
			}
			return String.join("/", split);
		} catch (UnsupportedEncodingException ex) {
			LOGGER.error("Should not happen, UTF-8 should always be supported.", ex);
		}
		return string;
	}

}
