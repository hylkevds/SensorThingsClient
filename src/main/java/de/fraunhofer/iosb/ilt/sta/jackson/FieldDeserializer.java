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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.fraunhofer.iosb.ilt.swe.common.AbstractDataComponent;
import de.fraunhofer.iosb.ilt.swe.common.complex.Field;
import java.io.IOException;

/**
 *
 * @author scf
 */
public class FieldDeserializer extends JsonDeserializer<Field> {

    @Override
    public Field deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectNode value = p.readValueAsTree();
        JsonNode name = value.get("name");
        value.remove("name");
        AbstractDataComponent adc = ObjectMapperFactory.get().treeToValue(value, AbstractDataComponent.class);
        return new Field(name.asText(), adc);
    }

}
