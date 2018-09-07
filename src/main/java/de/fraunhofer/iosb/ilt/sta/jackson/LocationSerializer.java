package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.geojson.GeoJsonObject;

/**
 * Serializer for Location property.
 */
public class LocationSerializer extends StdSerializer<Object> {

    public LocationSerializer() {
        super(Object.class);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        if (value instanceof GeoJsonObject) {
            new ObjectMapper().writerFor(GeoJsonObject.class).writeValue(gen, value);
        } else {
            gen.writeObject(value);
        }
    }
}
