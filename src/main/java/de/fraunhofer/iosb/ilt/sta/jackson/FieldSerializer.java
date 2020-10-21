/*
 * Copyright (C) 2020 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
 * Karlsruhe, Germany.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.swe.common.AbstractDataComponent;
import de.fraunhofer.iosb.ilt.swe.common.complex.Field;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 *
 * @author scf
 */
public class FieldSerializer extends JsonSerializer<Field> {

    @Override
    public void serialize(Field field, JsonGenerator jg, SerializerProvider sp) throws IOException {
        JsonNode fieldNode = ObjectMapperFactory.get().valueToTree(field.getField());
        ((ObjectNode) fieldNode).put("name", field.getName());
        jg.writeObject(fieldNode);
    }

}
