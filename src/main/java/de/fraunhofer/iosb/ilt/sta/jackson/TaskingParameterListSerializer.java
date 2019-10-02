package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.fraunhofer.iosb.ilt.sta.model.tasking.parameter.TaskingParameter;
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
public class TaskingParameterListSerializer extends JsonSerializer<List<TaskingParameter>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskingParameterListSerializer.class);


    @Override
    public void serialize(List<TaskingParameter> parameters, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeFieldName("type");
        gen.writeString("DataRecord");
        gen.writeFieldName("field");
        gen.writeStartArray();
        for (int i = 0; i < parameters.size(); i++) {
            gen.writeObject(parameters.get(i));
            if (i < parameters.size()-1) {
                gen.writeRaw(",");
            }
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
