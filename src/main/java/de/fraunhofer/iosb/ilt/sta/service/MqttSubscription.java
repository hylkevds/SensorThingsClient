package de.fraunhofer.iosb.ilt.sta.service;

import java.util.function.Consumer;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Michael Jacoby
 */
public class MqttSubscription {

    private String topic;
    private Consumer<MqttMessage> handler;

    public MqttSubscription(String topic, Consumer<MqttMessage> handler) {
        this.topic = topic;
        this.handler = handler;
    }

    public Consumer<MqttMessage> getHandler() {
        return handler;
    }

    public void setHandler(Consumer<MqttMessage> handler) {
        this.handler = handler;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
