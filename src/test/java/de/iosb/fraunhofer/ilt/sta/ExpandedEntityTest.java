package de.iosb.fraunhofer.ilt.sta;

import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.query.ExpandedEntity;
import de.fraunhofer.iosb.ilt.sta.query.InvalidRelationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

public class ExpandedEntityTest {

    @Test // Thing/Locations
    public void testRelation_1_to_n_valid() {
        try {
            ExpandedEntity expandedEntity = ExpandedEntity.from(EntityType.THING, EntityType.LOCATIONS);
            assertEquals(EntityType.THING, expandedEntity.getDirectSibling());
            assertEquals("Thing/Locations", expandedEntity.toString());
        } catch (InvalidRelationException e) {
            fail(e.getMessage());
        }
    }

    @Test // Datastream/Sensors (invalid)
    public void testRelation_1_to_n_invalid() {
        assertThrows(InvalidRelationException.class,
                () -> ExpandedEntity.from(EntityType.DATASTREAM, EntityType.SENSORS));
    }

    @Test // Datastream/Sensor
    public void testRelation_1_to_1_valid() {
        try {
            ExpandedEntity expandedEntity = ExpandedEntity.from(EntityType.DATASTREAM, EntityType.SENSOR);
            assertEquals(EntityType.DATASTREAM, expandedEntity.getDirectSibling());
            assertEquals("Datastream/Sensor", expandedEntity.toString());
        } catch (InvalidRelationException e) {
            fail(e.getMessage());
        }
    }

    @Test // Thing/Datastreams/Sensor
    public void testRelations_1_to_n_to_1_valid() {
        try {
            ExpandedEntity expandedEntity = ExpandedEntity
                    .from(EntityType.THING, EntityType.DATASTREAMS, EntityType.SENSOR);
            assertEquals(EntityType.THING, expandedEntity.getDirectSibling());
            assertEquals("Thing/Datastreams/Sensor", expandedEntity.toString());
        } catch (InvalidRelationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSingleType() {
        try {
            ExpandedEntity.from(EntityType.THING);
        } catch (InvalidRelationException e) {
            fail(e.getMessage());
        }
    }

}