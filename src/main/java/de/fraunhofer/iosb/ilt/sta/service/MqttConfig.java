package de.fraunhofer.iosb.ilt.sta.service;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

/**
 *
 * @author Michael Jacoby
 */
public class MqttConfig {

    private String serverUri;
    private String clientId = this.getClass() + "-" + UUID.randomUUID();
    private MqttClientPersistence persistence = new MqttDefaultFilePersistence();
    private MqttConnectOptions options;

    public MqttConfig(String serverUri) {
        this.serverUri = serverUri;
    }

    public MqttConfig(String serverUri, String clientId) {
        this.serverUri = serverUri;
        this.clientId = clientId;
    }

    public MqttConfig(String serverUri, MqttClientPersistence persistence) {
        this.serverUri = serverUri;
        this.persistence = persistence;
    }

    public String getServerUri() {
        return serverUri;
    }

    public void setServerUri(String serverUri) {
        this.serverUri = serverUri;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MqttClientPersistence getPersistence() {
        return persistence;
    }

    public void setPersistence(MqttClientPersistence persistence) {
        this.persistence = persistence;
    }

    public MqttConnectOptions getOptions() {
        return options;
    }

    public void setOptions(MqttConnectOptions options) {
        this.options = options;
    }
}
