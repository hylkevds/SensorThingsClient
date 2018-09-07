package de.fraunhofer.iosb.ilt.sta.query;

/**
 * An exception to indicate that a relationship between two entities is invalid
 *
 * @author Nils Sommer
 *
 */
public class InvalidRelationException extends Exception {

    private static final long serialVersionUID = 5634858799613018139L;

    public InvalidRelationException() {
    }

    public InvalidRelationException(String message) {
        super(message);
    }

    public InvalidRelationException(Throwable cause) {
        super(cause);
    }

    public InvalidRelationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRelationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
