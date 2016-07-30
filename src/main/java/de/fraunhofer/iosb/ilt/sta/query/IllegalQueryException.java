package de.fraunhofer.iosb.ilt.sta.query;

/**
 * This type represents failures caused by illegal queries.
 *
 * @author Nils Sommer
 *
 */
public class IllegalQueryException extends Exception {

	private static final long serialVersionUID = -3574526953449714406L;

	public IllegalQueryException() {
		super();
	}

	public IllegalQueryException(String message) {
		super(message);
	}

	public IllegalQueryException(Throwable cause) {
		super(cause);
	}

	public IllegalQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalQueryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
