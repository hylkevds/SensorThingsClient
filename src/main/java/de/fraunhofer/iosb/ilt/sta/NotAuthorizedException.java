package de.fraunhofer.iosb.ilt.sta;

/**
 * The exception that is thrown when the service returns a 401 Unauthorized.
 *
 * @author scf
 */
public class NotAuthorizedException extends StatusCodeException {

    public NotAuthorizedException(String url, String statusMessage, String returnedContent) {
        super(url, 401, statusMessage, returnedContent);
    }
}
