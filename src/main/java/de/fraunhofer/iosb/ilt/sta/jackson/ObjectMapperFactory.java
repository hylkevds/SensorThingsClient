package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fraunhofer.iosb.ilt.swe.common.AbstractDataComponent;
import de.fraunhofer.iosb.ilt.swe.common.AbstractSWEIdentifiable;
import de.fraunhofer.iosb.ilt.swe.common.complex.DataRecord;
import de.fraunhofer.iosb.ilt.swe.common.constraint.AbstractConstraint;

/**
 * Factory for ObjectMapper instances. Keeps track of configuration.
 *
 * @author Nils Sommer, Hylke van der Schaaf, Michael Jacoby
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
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new EntityModule());
            mapper.registerModule(new SweCommonModule());
            mapper.addMixIn(DataRecord.class, DataRecordMixin.class);
            mapper.addMixIn(AbstractDataComponent.class, AbstractDataComponentMixin.class);
            mapper.addMixIn(AbstractSWEIdentifiable.class, AbstractSWEIdentifiableMixin.class);
            mapper.addMixIn(AbstractConstraint.class, AbstractConstraintMixin.class);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        return mapper;
    }

}
