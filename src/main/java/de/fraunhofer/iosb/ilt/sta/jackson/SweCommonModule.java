package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.fraunhofer.iosb.ilt.swe.common.complex.Field;

/**
 *
 * @author Michael Jacoby
 */
public class SweCommonModule extends SimpleModule {

    private static final long serialVersionUID = -667345867846254501L;

    public SweCommonModule() {
        super(new Version(0, 0, 1, null, null, null));
        addSerializer(Field.class, new FieldSerializer());
        addDeserializer(Field.class, new FieldDeserializer());
    }
}
