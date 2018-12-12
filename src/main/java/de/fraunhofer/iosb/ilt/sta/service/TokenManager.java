package de.fraunhofer.iosb.ilt.sta.service;

import org.apache.http.HttpRequest;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * The TokenManager Interface. Before each request is sent to the Service, the
 * TokenManager has the opportunity to modify the request and add any headers
 * required for Authentication and Authorisation.
 *
 * @author scf
 */
public interface TokenManager {

    /**
     * Add any headers to the request that are required Authentication and
     * Authorisation.
     *
     * @param <T> The request type. Inferred from the Request.
     * @param request The request to modify.
     */
    public <T extends HttpRequest> void addAuthHeader(T request);

    /**
     * Set the HTTP client this TokenManager uses to fetch tokens.
     *
     * @param client The CloseableHttpClient to use for fetching Tokens.
     * @return this TokenManager
     */
    public <T extends TokenManager> T setHttpClient(CloseableHttpClient client);

    /**
     * Get the HTTP client this TokenManager uses to fetch tokens.
     *
     * @return The HTTP client this TokenManager uses to fetch tokens.
     */
    public CloseableHttpClient getHttpClient();

}
