package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import static de.fraunhofer.iosb.ilt.sta.model.Entity.AT_IOT_COUNT;
import static de.fraunhofer.iosb.ilt.sta.model.Entity.AT_IOT_NEXT_LINK;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityListDeserializer<T extends Entity<T>> extends StdDeserializer<EntityList<T>> implements ContextualDeserializer {

    private static final long serialVersionUID = 8376494553925868647L;
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityListDeserializer.class);

    private Class<T> type;

    public EntityListDeserializer() {
        super(EntityList.class);
    }

    public EntityListDeserializer(Class<T> type) {
        super(EntityList.class);
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        final JavaType wrapperType;
        if (property == null) {
            wrapperType = ctxt.getContextualType();
        } else {
            wrapperType = property.getType();
        }

        JavaType valueType = wrapperType.containedType(0);
        return new EntityListDeserializer(valueType.getRawClass());
    }

    @Override
    public EntityList<T> deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        final EntityList<T> entities = new EntityList<>(EntityType.listForClass(type));

        JsonToken currentToken = parser.getCurrentToken();
        if (currentToken == JsonToken.START_ARRAY) {
            // Direct array, probably expanded.
            JsonToken nextToken = parser.nextToken();
            if (nextToken != JsonToken.END_ARRAY) {
                Iterator<T> readValuesAs = parser.readValuesAs(type);
                while (readValuesAs.hasNext()) {
                    T next = readValuesAs.next();
                    entities.add(next);
                }
            }
        } else {
            boolean done = false;
            while (!done) {
                JsonToken mainToken = parser.nextToken();
                switch (mainToken) {
                    case END_OBJECT:
                        done = true;
                        break;

                    case FIELD_NAME:
                        String fieldName = parser.getCurrentName();
                        JsonToken valueToken = parser.nextToken();
                        switch (fieldName) {
                            case AT_IOT_COUNT:
                                entities.setCount(parser.getValueAsLong());
                                break;

                            case AT_IOT_NEXT_LINK:
                                try {
                                    entities.setNextLink(URI.create(parser.getValueAsString()));
                                } catch (IllegalArgumentException e) {
                                    LOGGER.warn("@iot.nextLink field contains malformed URI", e);
                                }
                                break;

                            case "value":
                                if (valueToken == JsonToken.START_ARRAY) {
                                    JsonToken nextToken = parser.nextToken();
                                    if (nextToken == JsonToken.END_ARRAY) {
                                        // Empty array.
                                    } else {
                                        Iterator<T> values = parser.readValuesAs(type);
                                        while (values.hasNext()) {
                                            T value = values.next();
                                            entities.add(value);
                                        }
                                    }
                                } else {
                                    LOGGER.warn("value field is not an array!");
                                }
                                break;

                        }
                        break;

                    default:
                        LOGGER.warn("Unhandled token: {}", mainToken);
                }
            }
        }
        return entities;
    }

}
