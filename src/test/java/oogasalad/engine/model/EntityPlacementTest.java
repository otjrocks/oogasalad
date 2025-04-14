package oogasalad.engine.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityPlacementTest {

  private EntityType testEntityType;
  private EntityPlacement testPlacement;

  @BeforeEach
  public void setUp() {
    testEntityType = new EntityType("Enemy", null, null, null);
    testPlacement = new EntityPlacement(testEntityType, 30.0, 40.0, "Default");
  }

  @Test
  public void testConstructor() {
    assertEquals(testEntityType, testPlacement.getType());
    assertEquals(30.0, testPlacement.getX());
    assertEquals(40.0, testPlacement.getY());
  }

  @Test
  public void testGetSetEntityData() {
    EntityType newEntityType = new EntityType("Player", null, null, null);
    testPlacement.setResolvedEntityType(newEntityType);
    assertEquals(newEntityType, testPlacement.getType());
    assertEquals("Player", testPlacement.getType().type());
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
  public void testToString() {
    String expected = "EntityPlacement{entityData=Enemy, x=30.0, y=40.0}";
    assertEquals(expected, testPlacement.toString());
  }

  @Test
  public void testNullEntityData() {
    Exception exception = assertThrows(NullPointerException.class, () -> {
      new EntityPlacement(null, 10.0, 20.0, "Default").toString();
    });

    assertNotNull(exception);
  }
}