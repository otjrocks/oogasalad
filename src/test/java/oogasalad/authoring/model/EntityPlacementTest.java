package oogasalad.authoring.model;

import static org.junit.jupiter.api.Assertions.*;
import oogasalad.engine.model.EntityData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class EntityPlacementTest {

    private EntityData testEntityData;
    private EntityPlacement testPlacement;

    @BeforeEach
    public void setUp() {
        testEntityData = new EntityData();
        testEntityData.setType("Enemy");
        testEntityData.setImagePath("/images/enemy.png");
        testEntityData.setControlType("AI");
        testEntityData.setEffect("Damage");
        testEntityData.setInitialX(10.0);
        testEntityData.setInitialY(20.0);

        testPlacement = new EntityPlacement(testEntityData, 30.0, 40.0);
    }

    @Test
    public void testConstructor() {
        assertEquals(testEntityData, testPlacement.getEntityData());
        assertEquals(30.0, testPlacement.getX());
        assertEquals(40.0, testPlacement.getY());
    }

    @Test
    public void testGetSetEntityData() {
        EntityData newEntityData = new EntityData();
        newEntityData.setType("Player");

        testPlacement.setEntityData(newEntityData);

        assertEquals(newEntityData, testPlacement.getEntityData());
        assertEquals("Player", testPlacement.getEntityData().getType());
    }

    @Test
    public void testGetSetX() {
        testPlacement.setX(50.0);

        assertEquals(50.0, testPlacement.getX());
    }

    @Test
    public void testGetSetY() {
        testPlacement.setY(60.0);

        assertEquals(60.0, testPlacement.getY());
    }

    @Test
    public void testMoveTo() {
        testPlacement.moveTo(70.0, 80.0);

        assertEquals(70.0, testPlacement.getX());
        assertEquals(80.0, testPlacement.getY());
    }

    @Test
    public void testUpdateInitialPosition() {
        testPlacement.setX(90.0);
        testPlacement.setY(100.0);

        testPlacement.updateInitialPosition();

        assertEquals(90.0, testPlacement.getEntityData().getInitialX());
        assertEquals(100.0, testPlacement.getEntityData().getInitialY());
    }

    @Test
    public void testToString() {
        String expected = "EntityPlacement{entityData=Enemy, x=30.0, y=40.0}";
        assertEquals(expected, testPlacement.toString());
    }

    @Test
    public void testNullEntityData() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new EntityPlacement(null, 10.0, 20.0).toString();
        });

        assertNotNull(exception);
    }
}