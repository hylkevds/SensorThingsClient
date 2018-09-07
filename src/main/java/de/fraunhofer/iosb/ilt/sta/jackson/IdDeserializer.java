package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.fraunhofer.iosb.ilt.sta.model.Id;
import de.fraunhofer.iosb.ilt.sta.model.IdLong;
import de.fraunhofer.iosb.ilt.sta.model.IdString;
import java.io.IOException;

/**
 * Deserializer for ISO-8601 intervals to
 * {@link org.threeten.extra.Interval Interval} instances.
 *
 * @author Nils Sommer
 *
 */
public class IdDeserializer extends StdDeserializer<Id> {

    private static final long serialVersionUID = 3674342381623629828L;

    public IdDeserializer() {
        super(Id.class);
    }

    // org.threeten.extra.Interval doesn't support time zone setups yet:
    // https://github.com/ThreeTen/threeten-extra/issues/66
    // Patch submitted.
    @Override
    public Id deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        JsonNode node = parser.getCodec().readTree(parser);
        if (node.isNumber()) {
            return new IdLong(node.asLong());
        }
        return new IdString(node.asText());
    }
}
