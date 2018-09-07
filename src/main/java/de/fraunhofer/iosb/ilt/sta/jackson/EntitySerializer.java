package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serializer for SensorThings Entities.
 *
 * @author Nils Sommer
 *
 */
public class EntitySerializer extends JsonSerializer<Entity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntitySerializer.class);

    @Override
    public void serialize(Entity entity, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        gen.writeStartObject();

        SerializationConfig config = serializers.getConfig();
        final BasicBeanDescription beanDesc = config.introspect(serializers.constructType(entity.getClass()));

        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        for (BeanPropertyDefinition property : properties) {
            Object rawValue = property.getAccessor().getValue(entity);
            // TODO: Check if prop is collection

            // Write field if not null.
            if (rawValue != null) {
                if (rawValue instanceof Entity) {
                    Entity subEntity = (Entity) rawValue;
                    if (subEntity.getId() != null) {
                        // It's a referenced entity. -> <Entity>: { "@iot.id": <id> }
                        gen.writeFieldName(rawValue.getClass().getSimpleName());
                        gen.writeStartObject();
                        gen.writeFieldName("@iot.id");
                        ((Entity) rawValue).getId().writeTo(gen);
                        gen.writeEndObject();
                    } else {
                        gen.writeFieldName(rawValue.getClass().getSimpleName());
                        serialize(subEntity, gen, serializers);
                    }
                } else if (rawValue instanceof EntityList) {
                    EntityList entityList = (EntityList) rawValue;
                    if (entityList.isEmpty()) {
                        continue;
                    }
                    // Ignore collections during serialization.
                    gen.writeFieldName(entityList.getType().getName());
                    gen.writeStartArray();
                    for (Object sub : entityList) {
                        if (sub instanceof Entity) {
                            Entity subEntity = (Entity) sub;
                            if (subEntity.getId() == null) {
                                serialize(subEntity, gen, serializers);
                            } else {
                                gen.writeStartObject();
                                gen.writeFieldName("@iot.id");
                                subEntity.getId().writeTo(gen);
                                gen.writeEndObject();
                            }
                        }
                    }
                    gen.writeEndArray();
                    continue;
                } else {
                    JsonSerialize annotation = property.getField().getAnnotation(JsonSerialize.class);
                    JsonSerializer serializer = null;
                    if (annotation != null) {
                        try {
                            Class<? extends JsonSerializer> using = annotation.using();
                            serializer = using.newInstance();
                        } catch (InstantiationException | IllegalAccessException ex) {
                            LOGGER.warn("Could not instantiate serialiser specified in annotation.", ex);
                        }
                    }
                    TypeSerializer typeSerializer = serializers.findTypeSerializer(serializers.constructType(property.getAccessor().getRawType()));
                    BeanPropertyWriter writer = new BeanPropertyWriter(
                            property,
                            property.getAccessor(),
                            beanDesc.getClassAnnotations(),
                            property.getAccessor().getType(),
                            serializer,
                            typeSerializer, // will not be searched automatically
                            property.getAccessor().getType(),
                            true, // ignore null values
                            null);
                    try {
                        writer.serializeAsField(entity, gen, serializers);
                    } catch (Exception e) {
                        LOGGER.error("Failed to serialize entity.", e);
                    }
                }
            }
        }

        gen.writeEndObject();
    }
}
