package de.fraunhofer.iosb.ilt.sta.service;

import org.apache.http.HttpRequest;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * The TokenManager Interface. Before each request is sent to the Service, the
 * TokenManager has the opportunity to modify the request and add any headers
 * required for Authentication and Authorisation.
 *
 * @author scf
 * @param <T> The exact type of this token manager.
 */
public interface TokenManager<T extends TokenManager> {

    /**
     * Add any headers to the request that are required Authentication and
     * Authorisation.
     *
     * @param request The request to modify.
     */
    public void addAuthHeader(HttpRequest request);

    /**
     * Set the HTTP client this TokenManager uses to fetch tokens.
     *
     * @param client The CloseableHttpClient to use for fetching Tokens.
     * @return this TokenManager
     */
    public T setHttpClient(CloseableHttpClient client);

    /**
     * Get the HTTP client this TokenManager uses to fetch tokens.
     *
     * @return The HTTP client this TokenManager uses to fetch tokens.
     */
    public CloseableHttpClient getHttpClient();

}
