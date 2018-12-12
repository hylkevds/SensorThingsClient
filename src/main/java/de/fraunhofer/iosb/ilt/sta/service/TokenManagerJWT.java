package de.fraunhofer.iosb.ilt.sta.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Consts;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Calendar;

public class TokenManagerJWT implements TokenManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManagerJWT.class);

    private static final String AUTH_BODY_TEMPLATE = "{\"id\":\"%1$s\",\"key\":\"%2$s\"}";
    private String tokenServerUrl;
    private String jwtId;
    private String jwtKey;
    private CloseableHttpClient client;
    private String accessToken = "";
    private Calendar expireTime = Calendar.getInstance();
    /* Assume a Token expire time of 5 minutes unless the server specifies
     * otherwise.
     */
    private int expireDuration = 300;

    /**
     * Add any headers to the request that are required Authentication and
     * Authorisation.
     *
     * @param request The request to modify.
     */
    @Override
    public <T extends HttpRequest> void addAuthHeader(T request) {
        request.addHeader("Authorization", "Bearer " + getToken());
    }

    public boolean isExpired() { return expireTime.before(Calendar.getInstance()); }

    private String fetchToken() {
        String json = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost tokenRequest = new HttpPost(tokenServerUrl);
            tokenRequest.setHeader("Content-Type", "application/json");
            String body = String.format(AUTH_BODY_TEMPLATE, jwtId, jwtKey);
            tokenRequest.setEntity(new StringEntity(body));
            response = client.execute(tokenRequest);
            json = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        } catch (IOException ex) {
            LOGGER.error("Failed to fetch Token.", ex);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex2) {
                    LOGGER.error("Exception closing response after exception!", ex2);
                }
            }
        }
        return json;
    }

    public String getToken() {
        final String currentToken = accessToken;
        if (!currentToken.isEmpty() && !isExpired()) {
            return currentToken;
        }

        synchronized (this) {
            accessToken = "";
            String json = fetchToken();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode tree = objectMapper.readTree(json);
                if (tree.isObject()) {
                    JsonNode node = tree.get("token");
                    if (node == null) {
                        throw new IllegalStateException("Did not receive an access_token. Received: " + json);
                    }
                    accessToken = node.textValue();
                    validateToken(accessToken);

                    expireTime = Calendar.getInstance();
                    // If tokens are valid for less than 10 seconds assume they're expired.
                    // unless the token is valid for less than 10 seconds to begin with.
                    expireTime.add(Calendar.SECOND, Math.max(expireDuration - 10, 10));
                }

                LOGGER.debug("Token: {}", accessToken);

                return accessToken;
            } catch (IOException ex) {
                LOGGER.error("Failed to parse response.", ex);
                return null;
            }
        }
    }

    public boolean validateToken(String token) {
        // TODO: Implement token validation
        return true;
    }

    /**
     * Set the HTTP client this TokenManager uses to fetch tokens.
     *
     * @param client The CloseableHttpClient to use for fetching Tokens.
     * @return this TokenManager
     */
    @Override
    public TokenManagerJWT setHttpClient(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * Get the HTTP client this TokenManager uses to fetch tokens.
     *
     * @return The HTTP client this TokenManager uses to fetch tokens.
     */
    @Override
    public CloseableHttpClient getHttpClient() {
        return client;
    }

    /**
     * Set the URL to fetch tokens from. Usually in the form of
     * http://example.com/auth/realms/{realm}/protocol/openid-connect/token
     *
     * @param tokenServerUrl The URL to fetch tokens from.
     * @return this TokenManager
     */
    public TokenManagerJWT setTokenServerUrl(String tokenServerUrl) {
        this.tokenServerUrl = tokenServerUrl;
        return this;
    }

    /**
     * The ID to use for fetching tokens.
     *
     * @param jwtId The ID on the JWT auth server.
     * @return this TokenManager
     */
    public TokenManagerJWT setJwtId(String jwtId) {
        this.jwtId = jwtId;
        return this;
    }

    /**
     * Set the key to use for getting tokens.
     *
     * @param jwtKey The key to use for getting tokens.
     * @return this TokenManager
     */
    public TokenManagerJWT setJwtKey(String jwtKey) {
        this.jwtKey = jwtKey;
        return this;
    }
}
