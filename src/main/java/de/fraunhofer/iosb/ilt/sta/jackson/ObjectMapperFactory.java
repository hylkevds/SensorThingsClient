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
            ObjectMapper myMapper = new ObjectMapper();
            myMapper.registerModule(new JavaTimeModule());
            myMapper.registerModule(new EntityModule());
            myMapper.registerModule(new SweCommonModule());
            myMapper.addMixIn(DataRecord.class, DataRecordMixin.class);
            myMapper.addMixIn(AbstractDataComponent.class, AbstractDataComponentMixin.class);
            myMapper.addMixIn(AbstractSWEIdentifiable.class, AbstractSWEIdentifiableMixin.class);
            myMapper.addMixIn(AbstractConstraint.class, AbstractConstraintMixin.class);
            myMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            myMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
            myMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            mapper = myMapper;
        }
        return mapper;
    }

}
