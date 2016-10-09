package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.fraunhofer.iosb.ilt.sta.model.TimeObject;
import java.io.IOException;
import org.threeten.extra.Interval;

/**
 * Deserializer for TimeObject instances.
 */
public class TimeObjectDeserializer extends StdDeserializer<TimeObject> {

	private static final long serialVersionUID = 3674342381623629828L;

	public TimeObjectDeserializer() {
		super(Interval.class);
	}

	@Override
	public TimeObject deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return TimeObject.parse(((JsonNode) parser.getCodec().readTree(parser)).asText());
	}
}
