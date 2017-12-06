package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serializer for SensorThings Entities.
 *
 * @author Nils Sommer
 *
 */
public class EntitySerializer extends JsonSerializer<Entity> {

	private static final Logger logger = LoggerFactory.getLogger(EntitySerializer.class);

	@Override
	public void serialize(Entity entity, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeStartObject();

		SerializationConfig config = serializers.getConfig();
		final BasicBeanDescription beanDesc = config.introspect(serializers.constructType(entity.getClass()));

		for (BeanPropertyDefinition def : beanDesc.findProperties()) {
			Object rawValue = def.getAccessor().getValue(entity);
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
					TypeSerializer typeSerializer = serializers.findTypeSerializer(serializers.constructType(def.getAccessor().getRawType()));
					BeanPropertyWriter writer = new BeanPropertyWriter(
							def,
							def.getAccessor(),
							beanDesc.getClassAnnotations(),
							def.getAccessor().getType(),
							null, // will be searched automatically
							typeSerializer, // will not be searched automatically
							def.getAccessor().getType(),
							true, // ignore null values
							null);
					try {
						writer.serializeAsField(entity, gen, serializers);
					} catch (Exception e) {
						logger.error("Failed to serialize entity.", e);
					}
				}
			}
		}

		gen.writeEndObject();
	}
}
