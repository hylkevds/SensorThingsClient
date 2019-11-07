package de.iosb.fraunhofer.ilt.sta;

import de.fraunhofer.iosb.ilt.sta.Utils;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
import de.fraunhofer.iosb.ilt.sta.model.builder.ObservedPropertyBuilder;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import de.fraunhofer.iosb.ilt.sta.service.TokenManagerJWT;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URI;
import java.net.URL;

public class TokenManagerJWTTest {
    private String staRootUrl;
    private String authUrl;
    private String jwtId;
    private String jwtKey;
    private boolean configured;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        configured = false;
        staRootUrl = System.getenv("STA_ROOT_URL");
        authUrl = System.getenv("AUTH_URL");
        jwtId = System.getenv("JWT_ID");
        jwtKey = System.getenv("JWT_KEY");
        if (staRootUrl != null && authUrl != null &&
                jwtId != null && jwtKey != null) {
            configured = true;
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testJwtAuth() throws Exception {
        if (configured) {
            URL serviceEndpoint = new URL(staRootUrl);
            SensorThingsService service = new SensorThingsService();
            service.setHttpClient(Utils.createInsecureHttpClient());
            service.setEndpoint(serviceEndpoint);
            TokenManagerJWT tokenMgr = new TokenManagerJWT()
                    .setJwtId(jwtId)
                    .setJwtKey(jwtKey)
                    .setTokenServerUrl(authUrl)
                    .setHttpClient(service.getHttpClient());
            service.setTokenManager(tokenMgr);
            ObservedProperty op = ObservedPropertyBuilder.builder()
                    .name("Test ObservedProperty")
                    .definition(URI.create("http://example.com/test_obs_prop"))
                    .description("Which describes the Test ObservedProperty")
                    .build();
            service.create(op);
            service.delete(op);
        }
    }
}
