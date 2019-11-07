package de.fraunhofer.iosb.ilt.sta.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @author Michael Jacoby
 */
public class ServerSettings {

    public static final String TAG_SERVER_SETTINGS = "serverSettings";
    public static final String TAG_EXTENSIONS = "extensions";
    public static final String TAG_MQTT_ENDPOINTS = "endpoints";
    private Map<Extension, Map<String, Object>> extensions;

    public ServerSettings() {
        extensions = new HashMap<>();
    }

    public Map<Extension, Map<String, Object>> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<Extension, Map<String, Object>> extensions) {
        this.extensions = extensions;
    }

    public enum Extension {
        ACTUATION("actuation"),
        MULTIDATASTREAM("multiDatastream"),
        MQTT("mqtt");

        private String name;

        private Extension(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static Extension fromName(String name) {
            Optional<Extension> result = Stream.of(values()).filter(x -> x.getName().equals(name)).findAny();
            if (result.isPresent()) {
                return result.get();
            }
            return null;
        }
    }
}
