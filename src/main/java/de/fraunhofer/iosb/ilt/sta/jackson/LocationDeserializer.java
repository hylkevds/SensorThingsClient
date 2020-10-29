package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ValueNode;
import java.io.IOException;
import org.geojson.GeoJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deserializer for TimeObject instances.
 */
public class LocationDeserializer extends StdDeserializer<Object> {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationDeserializer.class);
    private static final long serialVersionUID = 3674342381623629824L;

    public LocationDeserializer() {
        super(Object.class);
    }

    @Override
    public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        final ObjectCodec codec = parser.getCodec();
        TreeNode tree = codec.readTree(parser);
        return tryConvertTree(tree, codec);
    }

    private static Object tryConvertTree(TreeNode tree, ObjectCodec parser) {
        if (tree.isValueNode()) {
            ValueNode value = (ValueNode) tree;
            if (value.isTextual()) {
                return value.asText();
            }
            if (value.isNumber()) {
                return value.numberValue();
            }
            if (value.isBoolean()) {
                return value.asBoolean();
            }
            if (value.isNull()) {
                return null;
            }
        }
        try {
            return parser.treeToValue(tree, GeoJsonObject.class);
        } catch (IOException e) {
            LOGGER.debug("Not a geoJsonObject.");
        }
        return tree;
    }

    public static Object deserialize(String text, ObjectMapper mapper) throws JsonProcessingException {
        TreeNode tree = mapper.readTree(text);
        return tryConvertTree(tree, mapper);
    }
}
