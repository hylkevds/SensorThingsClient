package de.iosb.fraunhofer.ilt.sta;

import de.fraunhofer.iosb.ilt.sta.model.*;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotEquals;

public class EntityMethodTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testActuatorMethods() {
        Actuator actuator = new Actuator();
        actuator.setDescription("description text here");
        actuator.setName("name");
        EntityList<TaskingCapability> emptyEntityList = new EntityList<>(EntityType.TASKING_CAPABILITIES);
        assertEquals(emptyEntityList.size(), actuator.getCapabilities().size());
        assertEquals("name", actuator.getName());
        assertEquals("description text here", actuator.getDescription());
        actuator.setNumber(new IdLong(1L));
        assertEquals(new IdLong(1L), actuator.getNumber());
        assertEquals(actuator.getNumber(), actuator.getId());
        actuator.setEncodingType("String");
        actuator.setMetadata("metadata here read me");
        assertEquals("String", actuator.getEncodingType());
        assertEquals("metadata here read me", actuator.getMetadata());

/***************************** Equals tests ***********************************************/
        Actuator testActuator1 = new Actuator();
        Actuator testActuator2 = new Actuator();
        Actuator testActuator3 = null;
        Sensor testSensor = new Sensor();
        assertFalse(testActuator1.equals(testSensor));
        assertFalse(testActuator1.equals(testActuator3));
        assert(testActuator1.equals(testActuator1));
        assert(testActuator1.equals(testActuator2));
        testActuator2.setMetadata("false");
        assertFalse(testActuator1.equals(testActuator2));
        testActuator1.setMetadata("false");
        assertEquals(testActuator1, testActuator2);
        testActuator2.setEncodingType("String");
        assertFalse(testActuator1.equals(testActuator2));
        testActuator1.setEncodingType("String");
        assertEquals(testActuator1, testActuator2);
        testActuator2.setDescription("another actuator");
        assertFalse(testActuator1.equals(testActuator2));
        testActuator1.setDescription("another actuator");
        assertEquals(testActuator1, testActuator2);
        testActuator2.setName("actuator");
        assertFalse(testActuator1.equals(testActuator2));
        testActuator1.setName("actuator");
        assertEquals(testActuator1,testActuator2);

/******************** END Equals Tests ***********************************************/

        assertEquals(testActuator1.hashCode(), testActuator2.hashCode());
        testActuator2.setDescription("another description");
        assertNotEquals(testActuator1.hashCode(),testActuator2.hashCode());

        testActuator1.setNumber(new IdLong(1L));
        Actuator testActuator4 = testActuator1.withOnlyId();
        testActuator3 = new Actuator();
        testActuator3.setNumber(new IdLong(1L));
        assertEquals(testActuator3, testActuator4);
    }

    @Test
    public void testTaskMethods() {
        Task testTask1 = new Task();
        assertEquals("UNKNOWN", testTask1.getState());
        testTask1.setState(TaskState.NEW);
        assertEquals("NEW", testTask1.getState());
        testTask1.setState(TaskState.FAILED);
        assertEquals("FAILED", testTask1.getState());

        Task testTask2 = new Task();
        testTask2.setState(TaskState.FAILED);
        Actuator testActuator = new Actuator();

        assertEquals(testTask1, testTask2);
        assertNotEquals(testTask1, testActuator);
        assertEquals(testTask1, testTask2);
        assertEquals(testTask1.hashCode(), testTask1.hashCode());
        testTask2.setId(new IdLong(1L));
        testTask1.setId(new IdLong(1L));
        testTask1 = testTask1.withOnlyId();
        testTask2 = testTask2.withOnlyId();
        assertEquals(testTask1, testTask2);

        assertNull(testTask1.getLastChange());
        LocalDateTime lastChange = LocalDateTime.of(2000, 12, 20, 21,30);
        testTask1.setLastChange(lastChange);
        assertEquals(lastChange, testTask1.getLastChange());
    }

    @Test
    public void testTaskingCapabilityMethods() {
        Map<String,Object> properties = new HashMap<>();
        TaskingCapability capability1 = new TaskingCapability("name", "description", properties);

        assertEquals(capability1, capability1);
        TaskingCapability capability2 = null;
        assertNotEquals(capability1, capability2);
        Actuator actuator = new Actuator();
        assertNotEquals(capability1, actuator);
        capability2 = new TaskingCapability();
        assertNotEquals(capability1, capability2);
        capability2.setName("name");
        assertNotEquals(capability1, capability2);
        capability2.setDescription("description");
        assertNotEquals(capability1, capability2);

        capability1.setActuator(actuator);


        Map<String, Object> parameter = new HashMap<>();
        capability1.createTask(parameter);
        capability1.setId(new IdLong(1L));
        capability2.setId(new IdLong(1L));
        capability1 = capability1.withOnlyId();
        capability2 = capability2.withOnlyId();
        assertEquals(capability1, capability2);
        assertEquals(capability1.hashCode(), capability1.hashCode());
    }
}
