package oogasalad.engine.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import oogasalad.engine.model.api.EntityFactory;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameMapImplTest {

  private final int width = 10;
  private final int height = 10;
  private GameMap myGameMap;
  private Entity myEntity;

  @BeforeEach
  void setUp() {
    myGameMap = new GameMapImpl(width, height);
    EntityData data = new EntityData();
    data.setInitialX(5);
    data.setInitialY(5);
    myEntity = EntityFactory.createEntity(data);
  }

  @Test
  void addEntity_attemptAddingValidEntity_Success() {
    assertDoesNotThrow(() -> myGameMap.addEntity(myEntity));
    assertEquals(myEntity, myGameMap.iterator().next());
  }

  @Test
  void addEntity_attemptAddingEntityOutOfBounds_ThrowException() {
    myEntity.getEntityData().setInitialX(width + 1);
    myEntity.getEntityData().setInitialY(height + 1);
    assertThrows(InvalidPositionException.class, () -> myGameMap.addEntity(myEntity));
  }

  @Test
  void removeEntity_attemptRemovingValidEntity_Success() {
    assertDoesNotThrow(() -> myGameMap.addEntity(myEntity));
    assertDoesNotThrow(() -> myGameMap.removeEntity(myEntity));
    assertFalse(myGameMap.iterator().hasNext());
  }

  @Test
  void removeEntity_attemptRemovingNonExistentEntity_ThrowException() {
    assertThrows(EntityNotFoundException.class, () -> myGameMap.removeEntity(myEntity));
  }


  @Test
  void getEntityAt_attemptGetEntityAt_ReturnsEntityAtPosition() {
    assertDoesNotThrow(() -> myGameMap.addEntity(myEntity));
    var ref = new Object() {
      Entity result;
    };
    assertDoesNotThrow(
        () -> ref.result = myGameMap.getEntityAt((int) myEntity.getEntityData().getInitialX(),
            (int) myEntity.getEntityData().getInitialY()));
    assertEquals(myEntity, ref.result);
  }

  @Test
  void getEntityAt_attemptGetOfNonExistentEntity_ThrowException() {
    assertDoesNotThrow(() -> myGameMap.addEntity(myEntity));
    assertThrows(EntityNotFoundException.class, () -> myGameMap.getEntityAt(1, 1));
  }


  @Test
  void iterator_ensureEntityIteratorContainsAddedEntities_Success() {
    assertDoesNotThrow(() -> myGameMap.addEntity(myEntity));
    Entity secondEntity = EntityFactory.createEntity(new EntityData());
    assertDoesNotThrow(() -> myGameMap.addEntity(secondEntity));
    Iterator<Entity> iterator = myGameMap.iterator();
    while (iterator.hasNext()) {
      Entity next = iterator.next();
      assertTrue(next.equals(secondEntity) || next.equals(myEntity));
    }
  }

  @Test
  void getWidth_attemptGetWidth_ReturnsWidth() {
    assertEquals(width, myGameMap.getWidth());
  }

  @Test
  void getHeight_attemptGetHeight_ReturnsHeight() {
    assertEquals(height, myGameMap.getHeight());
  }

}