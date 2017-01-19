package de.fraunhofer.iosb.ilt.sta.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.xml.bind.DatatypeConverter;
import org.apache.http.Consts;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

/**
 * A TokenManager for JsonWebTokens used in OpenID Connect authentication.
 *
 * @author scf
 */
public class TokenManager {

	/**
	 * The logger for this class.
	 */
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TokenManager.class);
	private String tokenServerUrl;
	private String clientId;
	private String userName;
	private String password;
	private CloseableHttpClient client;
	private String accessToken = "";
	private String refreshToken = "";
	private String keyType;
	private byte[] apiKeyBytes;
	/**
	 * Assume a Token expire time of 5 minutes unless the server specifies
	 * otherwise.
	 */
	private int expireDuration = 300;
	private Calendar expireTime = Calendar.getInstance();
	/**
	 * Assume a RefreshToken expire time of 30 minutes unless the server
	 * specifies otherwise.
	 */
	private int refreshExpireDuration = 1800;
	private Calendar refreshExpireTime = Calendar.getInstance();
	private boolean autoRefresh;
	private Timer timer;
	private TimerTask refreshTask;

	public TokenManager() {
	}

	public <T extends HttpRequest> T addAuthHeader(T request) {
		request.addHeader("Authorization", "Bearer " + getToken());
		return request;
	}

	public boolean isExpired() {
		return expireTime.before(Calendar.getInstance());
	}

	public String fetchTokenUsingPassword() {
		String json = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost tokenRequest = new HttpPost(tokenServerUrl);
			List<NameValuePair> parameters = new ArrayList<>();
			parameters.add(new BasicNameValuePair("grant_type", "password"));
			parameters.add(new BasicNameValuePair("client_id", clientId));
			parameters.add(new BasicNameValuePair("username", userName));
			parameters.add(new BasicNameValuePair("password", password));
			tokenRequest.setEntity(new UrlEncodedFormEntity(parameters));

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

	public String fetchTokenUsingRefreshToken() {
		String json = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost tokenRequest = new HttpPost(tokenServerUrl);
			List<NameValuePair> parameters = new ArrayList<>();
			parameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
			parameters.add(new BasicNameValuePair("refresh_token", refreshToken));
			tokenRequest.setEntity(new UrlEncodedFormEntity(parameters));

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

	public synchronized String getToken() {
		if (!accessToken.isEmpty() && !isExpired()) {
			return accessToken;
		}

		accessToken = "";
		String json = null;
		if (!refreshToken.isEmpty()) {
			json = fetchTokenUsingRefreshToken();
		}
		if (json == null) {
			json = fetchTokenUsingPassword();
		}

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode tree = objectMapper.readTree(json);
			if (tree.isObject()) {
				JsonNode node = tree.get("access_token");
				if (node == null) {
					throw new IllegalStateException("Failed to fetch token: " + json);
				}
				accessToken = node.textValue();
				validateToken(accessToken);

				refreshToken = tree.get("refresh_token").textValue();
				validateToken(refreshToken);

				node = tree.get("expires_in");
				if (node.isNumber() && node.canConvertToInt()) {
					expireDuration = node.intValue();
				}
				expireTime = Calendar.getInstance();
				// If tokens are valid for less than 10 seconds assume they're expired.
				// unless the token is valid for less than 10 seconds to begin with.
				expireTime.add(Calendar.SECOND, Math.max(expireDuration - 10, 10));

				node = tree.get("refresh_expires_in");
				if (node.isNumber() && node.canConvertToInt()) {
					refreshExpireDuration = node.intValue();
				}
				checkAutoRefreshTimer();
			}

			LOGGER.debug("Token: {}", accessToken);
			LOGGER.debug("RefreshToken: {}", refreshToken);

			return accessToken;
		} catch (IOException ex) {
			LOGGER.error("Failed to parse response.", ex);
			return null;
		}
	}

	private void checkAutoRefreshTimer() {
		if (!autoRefresh) {
			return;
		}

		if (timer == null) {
			timer = new Timer("Autorefresh", true);
		}
		Calendar targetTime = Calendar.getInstance();
		// If tokens are valid for less than 20 seconds assume they're expired.
		// unless the token is valid for less than 20 seconds to begin with.
		targetTime.add(Calendar.SECOND, Math.max(refreshExpireDuration - 20, 20));
		if (refreshExpireTime.before(targetTime)) {
			// The old refresh time is before the one we have, so we cancel the old one.
			refreshTask.cancel();
			refreshTask = null;
			refreshExpireTime = targetTime;
		}

		if (refreshTask == null) {
			refreshTask = new TimerTask() {
				@Override
				public void run() {
					autoRefresh();
				}
			};
			timer.schedule(refreshTask, targetTime.getTime());
		}

	}

	private void autoRefresh() {
		LOGGER.info("Auto-Refreshing the token.");
		getToken();
	}

	public boolean validateToken(String token) {
		try {
			if (Jwts.parser().isSigned(token)) {
				if (keyType != null) {
					X509EncodedKeySpec spec = new X509EncodedKeySpec(apiKeyBytes);
					KeyFactory fact = KeyFactory.getInstance(keyType);
					PublicKey key = fact.generatePublic(spec);
					Jwts.parser().setSigningKey(key).parse(token);
				} else if (apiKeyBytes != null) {
					Jwts.parser().setSigningKey(apiKeyBytes).parse(token);
				} else {
					LOGGER.debug("Can not validate token, please set the signing key.");
				}
			} else {
				Jwts.parser().parse(token);
			}
			return true;
		} catch (SignatureException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.error("Token failed signature!", e);
		}
		return false;
	}

	/**
	 * Set the HTTP client this TokenManager uses to fetch tokens.
	 *
	 * @param client The CloseableHttpClient to use for fetching Tokens.
	 * @return this TokenManager
	 */
	public TokenManager setHttpClient(CloseableHttpClient client) {
		this.client = client;
		return this;
	}

	/**
	 * Get the HTTP client this TokenManager uses to fetch tokens.
	 *
	 * @return The HTTP client this TokenManager uses to fetch tokens.
	 */
	public CloseableHttpClient getHttpClient() {
		return client;
	}

	/**
	 * Set the URL to fetch tokens from. Usually in the form of
	 * http://example.com/auth/realms/{realm}/protocol/openid-connect/token
	 *
	 * @param tokenServerUrl
	 * @return this TokenManager
	 */
	public TokenManager setTokenServerUrl(String tokenServerUrl) {
		this.tokenServerUrl = tokenServerUrl;
		return this;
	}

	/**
	 * The clientId to use for fetching tokens. This client has to be able to
	 * use Direct Access Grants on the Authentication server.
	 *
	 * @param clientId The clientId on the Auth Server.
	 * @return this TokenManager
	 */
	public TokenManager setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	/**
	 *
	 * @param userName
	 * @return this TokenManager
	 */
	public TokenManager setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	/**
	 *
	 * @param password
	 * @return this TokenManager
	 */
	public TokenManager setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * The public key to use for validating the token signature.
	 *
	 * @param signingKey The Base64 encoded public key.
	 * @return this TokenManager
	 */
	public TokenManager setSigningKey(String signingKey) {
		apiKeyBytes = DatatypeConverter.parseBase64Binary(signingKey);
		return this;
	}

	/**
	 * The type of public key (RSA or DSA).
	 *
	 * @param keyType The type of public key (RSA or DSA).
	 * @return this TokenManager
	 */
	public TokenManager setKeyType(String keyType) {
		this.keyType = keyType;
		return this;
	}

	/**
	 * Set a refreshToken. If you do not want to pass a username and password,
	 * you can instead fetch a token yourself, and pass the refresh token to the
	 * TokenManager. The TokenManager will then use this refreshToken to fetch
	 * an actual token.
	 *
	 * @param refreshToken The refreshToken to use instead of username/password.
	 * @return this TokenManager
	 */
	public TokenManager setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

	/**
	 * Set the expire duration of the refresh token. If autoRefresh is true, and
	 * no activity has occurred long enough for the refresh token to (almost)
	 * expire, an automatic refresh of the token will be done. If you set this
	 * you probably also want to set autoRefresh to true.
	 *
	 * @param refreshExpireDuration The expire duration of the refresh token.
	 * @return this TokenManager
	 */
	public TokenManager setRefreshExpireDuration(int refreshExpireDuration) {
		this.refreshExpireDuration = refreshExpireDuration;
		return this;
	}

	/**
	 * Turns autoRefresh on or off. If autoRefresh is true, and no activity has
	 * occurred long enough for the refresh token to (almost) expire, an
	 * automatic refresh of the token will be done. If the auth server does not
	 * specify the refresh token lifetime, you will have to set it with
	 * {@link #setRefreshExpireDuration(int)}
	 *
	 * @param autoRefresh Should autoRefresh happen or not.
	 * @return this TokenManager
	 */
	public TokenManager setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
		return this;
	}

}
