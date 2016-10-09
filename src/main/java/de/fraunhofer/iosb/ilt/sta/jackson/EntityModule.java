package de.fraunhofer.iosb.ilt.sta.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.TimeObject;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import org.threeten.extra.Interval;

/**
 * Module that contains all custom Serializer and Deserializer registrations
 * written as part of this library.
 *
 * @author Nils Sommer
 *
 */
public class EntityModule extends SimpleModule {

	private static final long serialVersionUID = -667555967846254500L;

	public EntityModule() {
		super(new Version(0, 0, 1, null, null, null));
		addSerializer(Entity.class, new EntitySerializer());
		addSerializer(UnitOfMeasurement.class, new UnitOfMeasurementSerializer());
		addSerializer(Interval.class, new IntervalSerializer());
		addSerializer(TimeObject.class, new TimeObjectSerializer());

		addDeserializer(Interval.class, new IntervalDeserializer());
		addDeserializer(TimeObject.class, new TimeObjectDeserializer());
	}
}
