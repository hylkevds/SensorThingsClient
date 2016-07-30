package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import java.io.IOException;

/**
 * Serializer for UnitOfMeasurement class.
 *
 * @author Nils Sommer
 *
 */
public class UnitOfMeasurementSerializer extends JsonSerializer<UnitOfMeasurement> {

	@Override
	public void serialize(UnitOfMeasurement value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeStartObject();
		gen.writeFieldName("name");
		gen.writeString(value.getName());
		gen.writeFieldName("symbol");
		gen.writeString(value.getSymbol());
		gen.writeFieldName("definition");
		gen.writeString(value.getDefinition());
		gen.writeEndObject();
	}
}
