/*
 * Copyright (C) 2016 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
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
package de.fraunhofer.iosb.ilt.sta.jackson.customtyping;

import de.fraunhofer.iosb.ilt.configurable.annotations.ConfigurableClass;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import org.reflections8.Reflections;

/**
 *
 * @author Michael Jacoby
 */
public class ConfigurableClassAnnotationTypeIdResolver implements TypeIdResolver {

    private JavaType superType;
    private static String foo;
    private static final Map<String, Class<?>> annnotatedClasses = new Reflections("de.fraunhofer.iosb.ilt.swe.common")
            .getTypesAnnotatedWith(ConfigurableClass.class, true).stream()
            .filter(x -> !x.getAnnotation(ConfigurableClass.class).jsonName().isEmpty())
            .collect(Collectors.toMap(x -> x.getAnnotation(ConfigurableClass.class).jsonName(), x -> x));  

    @Override
    public void init(JavaType baseType) {
        superType = baseType;
    }

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getAnnotation(ConfigurableClass.class).jsonName();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return value.getClass().getAnnotation(ConfigurableClass.class).jsonName();
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        if (!annnotatedClasses.containsKey(id)) {
            throw new RuntimeException(String.format("unkown type '%s'", id));
        }
        return context.constructSpecializedType(superType, annnotatedClasses.get(id));
    }

    @Override
    public String idFromBaseType() {
        ConfigurableClass annotation = superType.getClass().getAnnotation(ConfigurableClass.class);
        if (annotation != null) {
            return annotation.jsonName();
        }
        throw new RuntimeException("BaseType does not have annotation @ConfigurableClass");
    }

    @Override
    public String getDescForKnownTypeIds() {
        return annnotatedClasses.toString();
    }

}
