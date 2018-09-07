package de.fraunhofer.iosb.ilt.sta;

/**
 * An error for failed operations of a SensorThings service.
 *
 * @author Nils Sommer
 *
 */
public class ServiceFailureException extends Exception {

    private static final long serialVersionUID = -1365036034620856088L;

    public ServiceFailureException() {
    }

    public ServiceFailureException(String message) {
        super(message);
    }

    public ServiceFailureException(Throwable cause) {
        super(cause);
    }

    public ServiceFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceFailureException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
