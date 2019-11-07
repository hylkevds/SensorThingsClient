package de.fraunhofer.iosb.ilt.sta;

/**
 *
 * @author Michael Jacoby
 */
public class MqttException extends Exception {

    private static final long serialVersionUID = -1365036034620854171L;

    public MqttException(String message) {
        super(message);
    }

    public MqttException(Throwable cause) {
        super(cause);
    }

    public MqttException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqttException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
