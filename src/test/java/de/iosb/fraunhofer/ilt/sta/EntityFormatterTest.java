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
package de.iosb.fraunhofer.ilt.sta;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.Location;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author jab
 */
public class EntityFormatterTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void writeThing_Basic_Success() throws IOException {
		String expResult
				= "{\n"
				+ "\"@iot.id\": 1,\n"
				+ "\"Locations\": [],\n"
				+ "\"Datastreams\": [],\n"
				+ "\"HistoricalLocations\": [],\n"
				+ "\"name\": \"This thing is an oven.\",\n"
				+ "\"description\": \"This thing is an oven.\",\n"
				+ "\"properties\": {\n"
				+ "\"owner\": \"John Doe\",\n"
				+ "\"color\": \"Silver\"\n"
				+ "}\n"
				+ "}";
		Thing entity = new Thing();
		entity.setId(1L);
		entity.setLocations(new EntityList<>(EntityType.LOCATIONS));
		entity.setDatastreams(new EntityList<>(EntityType.DATASTREAMS));
		entity.setHistoricalLocations(new EntityList<>(EntityType.HISTORICAL_LOCATIONS));
		entity.setName("This thing is an oven.");
		entity.setDescription("This thing is an oven.");
		Map<String, Object> properties = new HashMap<>();
		properties.put("owner", "John Doe");
		properties.put("color", "Silver");
		entity.setProperties(properties);

		final ObjectMapper mapper = ObjectMapperFactory.get();
		String json = mapper.writeValueAsString(entity);
		assert (jsonEqual(expResult, json));
	}

	@Test
	public void writeThing_CompletelyEmpty_Success() throws IOException {
		String expResult
				= "{}";
		Thing entity = new Thing();
		final ObjectMapper mapper = ObjectMapperFactory.get();
		String json = mapper.writeValueAsString(entity);
		assert (jsonEqual(expResult, json));
	}

	@Test
	public void writeLocation_Basic_Success() throws Exception {
		String expResult
				= "{\n"
				+ "	\"@iot.id\": 1,\n"
				+ "	\"Things\": [],\n"
				+ "	\"HistoricalLocations\": [],\n"
				+ "	\"encodingType\": \"application/vnd.geo+json\""
				+ "}";
		Location entity = new Location();
		entity.setId(1L);
		entity.setThings(new EntityList(EntityType.THINGS));
		entity.setHistoricalLocations(new EntityList<>(EntityType.HISTORICAL_LOCATIONS));
		entity.setEncodingType("application/vnd.geo+json");
		final ObjectMapper mapper = ObjectMapperFactory.get();
		String json = mapper.writeValueAsString(entity);
		assert (jsonEqual(expResult, json));
	}

	private boolean jsonEqual(String string1, String string2) {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
		try {
			JsonNode json1 = mapper.readTree(string1);
			JsonNode json2 = mapper.readTree(string2);
			return json1.equals(json2);
		} catch (IOException ex) {
			Logger.getLogger(EntityFormatterTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

}
