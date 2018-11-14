package de.fraunhofer.iosb.ilt.sta;

/**
 * The exception that is thrown when the service returns a 404 not found.
 *
 * @author scf
 */
public class NotFoundException extends StatusCodeException {

    public NotFoundException(String statusMessage, String returnedContent) {
        super(404, statusMessage, returnedContent);
    }

}
