package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityListDeserializer<T extends Entity> extends StdDeserializer<EntityList<T>> {

	private static final long serialVersionUID = 8376494553925868647L;
	private static final Logger logger = LoggerFactory.getLogger(EntityListDeserializer.class);

	private Class<T> type;

	public EntityListDeserializer(Class<T> type) {
		super(EntityList.class);
		this.type = type;
	}

	@Override
	public EntityList<T> deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		final EntityList<T> entities = new EntityList<>(EntityType.listForClass(type));

		final JsonNode node = parser.getCodec().readTree(parser);

		if (node.has("@iot.count")) {
			entities.setCount(node.get("@iot.count").asLong());
		}

		if (node.has("@iot.nextLink")) {
			try {
				URI nextLink = new URI(node.get("@iot.nextLink").asText());
				entities.setNextLink(nextLink);
			} catch (URISyntaxException e) {
				logger.warn("@iot.nextLink field contains malformed URI", e);
			}
		}

		final Iterator<JsonNode> iter = node.get("value").elements();
		final ObjectMapper mapper = (ObjectMapper) parser.getCodec();

		while (iter.hasNext()) {
			JsonNode entityNode = iter.next();
			T entity = mapper.readValue(entityNode.toString(), this.type);
			entities.add(entity);
		}

		return entities;
	}

}
