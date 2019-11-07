package de.fraunhofer.iosb.ilt.sta.service;

import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @author Michael Jacoby
 */
public enum SensorThingsAPIVersion {
    v1_0(1, 0);

    public static SensorThingsAPIVersion fromString(String value) {
        Optional<SensorThingsAPIVersion> result = Stream.of(SensorThingsAPIVersion.values())
                .filter(x -> x.getUrlPattern().equals(value))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    private final int major;
    private final int minor;

    private SensorThingsAPIVersion(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public String getUrlPattern() {
        return String.format("v%d.%d", getMajor(), getMinor());
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }
}
