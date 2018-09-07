package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.threeten.extra.Interval;

/**
 * Serializer for intervals. Serializes the interval into an ISO-8601 interval
 * string representation.
 *
 * @author Nils Sommer
 *
 */
public class IntervalSerializer extends JsonSerializer<Interval> {

    @Override
    public void serialize(Interval interval, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        gen.writeString(interval.toString());
    }
}
