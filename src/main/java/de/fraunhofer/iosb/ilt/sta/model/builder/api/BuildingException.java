package de.fraunhofer.iosb.ilt.sta.model.builder.api;

/**
 * An {@link Exception} during the {@link Builder} process
 *
 * @author Aurelien Bourdon
 */
public class BuildingException extends RuntimeException {

    public BuildingException(final Throwable cause) {
        super(cause);
    }

    public BuildingException(final String message) {
        super(message);
    }

}
