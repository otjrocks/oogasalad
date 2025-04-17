package oogasalad.engine.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.api.EntityFactory;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import util.DukeApplicationTest;

@ExtendWith(MockitoExtension.class)
class GameMapImplTest extends DukeApplicationTest {

  private final int width = 10;
  private final int height = 10;
  private GameMap myGameMap;
  private Entity myEntity;
  private GameInputManager myInput;

  @Override
  public void start(Stage stage) {
    Group root = new Group();
    Scene myScene = new Scene(root, GameConfig.WIDTH, GameConfig.HEIGHT);
    myInput = new GameInputManager(myScene, root);
  }

  @BeforeEach
  void setUp() {
    myGameMap = new GameMapImpl(width, height);
    EntityType data = new EntityType("test", null, null, null, 1.0);
    EntityPlacement placement = new EntityPlacement(data, 5, 5, "Default");
    myEntity = EntityFactory.createEntity(myInput, placement, myGameMap);
  }

  @Test
  void addEntity_attemptAddingValidEntity_Success() {
    assertDoesNotThrow(() -> myGameMap.addEntity(myEntity));
    assertEquals(myEntity, myGameMap.iterator().next());
  }

  @Test
  void addEntity_attemptAddingEntityOutOfBounds_ThrowException() {
    myEntity.getEntityPlacement().setX(width + 1);
    myEntity.getEntityPlacement().setY(height + 1);
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
    Optional<Entity> result = myGameMap.getEntityAt((int) myEntity.getEntityPlacement().getX(),
        (int) myEntity.getEntityPlacement().getY());
    assertTrue(result.isPresent());
    assertEquals(myEntity, result.get());
  }

  @Test
  void getEntityAt_attemptGetOfNonExistentEntity_ReturnsEmptyOptional() {
    assertDoesNotThrow(() -> myGameMap.addEntity(myEntity));
    assertTrue(myGameMap.getEntityAt(1, 1).isEmpty());
  }


  @Test
  void iterator_ensureEntityIteratorContainsAddedEntities_Success() {
    assertDoesNotThrow(() -> myGameMap.addEntity(myEntity));
    EntityType data = new EntityType("test", null, null, null, 1.0);
    EntityPlacement placement = new EntityPlacement(data, 5, 5, "Default");
    Entity secondEntity = EntityFactory.createEntity(myInput, placement, myGameMap);
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

  @Test
  void isValidPosition_attemptGetValidPosition_ReturnsTrue() {
    assertTrue(myGameMap.isValidPosition(0, 0));
    assertTrue(myGameMap.isValidPosition(width - 1, height - 1));
  }

  @Test
  void isValidPosition_attemptGetInvalidPosition_ReturnsFalse() {
    assertFalse(myGameMap.isValidPosition(-1, 0));
    assertFalse(myGameMap.isValidPosition(0, -1));
    assertFalse(myGameMap.isValidPosition(width, height));
  }

  @Test
  void isNotBlocked_attemptNoEntityAtPosition_ReturnsTrue() {
    assertTrue(myGameMap.isNotBlocked("Anything", 1, 1));
  }

  @Test
  void isNotBlocked_attemptNonBlockingEntityAtPosition_ReturnsTrue()
      throws InvalidPositionException {
    // Create an entity that does NOT block anything
    EntityType data = new EntityType("nonBlocker", null, null, null, 1.0);
    EntityPlacement placement = new EntityPlacement(data, 3, 3, "Default");
    Entity nonBlockingEntity = EntityFactory.createEntity(myInput, placement, myGameMap);
    myGameMap.addEntity(nonBlockingEntity);

    assertTrue(myGameMap.isNotBlocked("anyType", 3, 3));
  }

  @Test
  void isNotBlocked_attemptBlockingEntityAtPosition_ReturnsFalse() throws InvalidPositionException {
    // Create an entity that blocks "Player"
    EntityType data = new EntityType("blocker", null, null, List.of("Player"), 1.0);
    EntityPlacement placement = new EntityPlacement(data, 4, 4, "Default");
    Entity blockingEntity = EntityFactory.createEntity(myInput, placement, myGameMap);
    myGameMap.addEntity(blockingEntity);

    assertFalse(myGameMap.isNotBlocked("Player", 4, 4));
  }
}