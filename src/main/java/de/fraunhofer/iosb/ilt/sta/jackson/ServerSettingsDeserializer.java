package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.fraunhofer.iosb.ilt.sta.service.ServerSettings;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Michael Jacoby
 */
public class ServerSettingsDeserializer extends StdDeserializer<ServerSettings> {

    private static final long serialVersionUID = 8376494553925868647L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSettingsDeserializer.class);

    public ServerSettingsDeserializer() {
        super(ServerSettings.class);
    }

    public ServerSettingsDeserializer(Class<?> type) {
        super(ServerSettings.class);
    }

    @Override
    public ServerSettings deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        ServerSettings result = new ServerSettings();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(parser);
        if (!root.has(ServerSettings.TAG_EXTENSIONS)) {
            context.reportInputMismatch(ServerSettings.class, "mandatory property '%s' missing", ServerSettings.TAG_EXTENSIONS);
        }
        root.get(ServerSettings.TAG_EXTENSIONS).elements().forEachRemaining(x -> {
            String extensionName = x.asText();
            ServerSettings.Extension extension = ServerSettings.Extension.fromName(extensionName);
            if (extension == null) {
                LOGGER.warn("ignording unkown server setting '" + extensionName + "'");
                return;
            }
            Map<String, Object> extensionProperties = mapper.convertValue(
                    root.get(extensionName),
                    context.getTypeFactory().constructMapLikeType(Map.class, String.class, Object.class));
            if (extensionProperties == null) {
                extensionProperties = new HashMap<>();
            }
            result.getExtensions().put(extension, extensionProperties);
        });
        return result;
    }
}
