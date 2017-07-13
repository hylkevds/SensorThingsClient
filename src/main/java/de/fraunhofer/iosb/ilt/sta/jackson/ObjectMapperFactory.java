package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;

/**
 * Factory for ObjectMapper instances. Keeps track of configuration.
 *
 * @author Nils Sommer, Hylke van der Schaaf
 *
 */
public final class ObjectMapperFactory {

	private static ObjectMapper mapper;

	private ObjectMapperFactory() {
	}

	/**
	 * Get a preconfigured, long living instance of {@link ObjectMapper} with
	 * all custom modules needed.
	 *
	 * @return the object mapper
	 */
	public static ObjectMapper get() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
			mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new EntityModule());
			// Write any date/time values as ISO-8601 formated strings.
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			final SimpleModule m = new SimpleModule(new Version(0, 0, 1, null, null, null));
			m.addDeserializer(EntityList.class, new EntityListDeserializer<>());
			mapper.registerModule(m);
		}

		return mapper;
	}

}
