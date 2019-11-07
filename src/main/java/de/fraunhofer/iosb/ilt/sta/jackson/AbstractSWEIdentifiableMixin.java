package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

/**
 *
 * @author Michael Jacoby
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(ConfigurableClassAnnotationTypeIdResolver.class)
public abstract class AbstractSWEIdentifiableMixin {

}
