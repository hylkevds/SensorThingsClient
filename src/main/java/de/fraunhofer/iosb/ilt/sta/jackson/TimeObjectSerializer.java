package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.fraunhofer.iosb.ilt.sta.model.TimeObject;
import java.io.IOException;

/**
 * Serializer for TimeObject class.
 */
public class TimeObjectSerializer extends JsonSerializer<TimeObject> {

	@Override
	public void serialize(TimeObject value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeString(value.toString());
	}
}
