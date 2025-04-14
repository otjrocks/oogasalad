package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContextRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsumeStrategyTest {

  private GameMap gameMap;
  private Entity entity1;
  private Entity entity2;
  private CollisionContextRecord context;
  private ConsumeStrategy strategy;

  @BeforeEach
  void setUp() {
    gameMap = mock(GameMap.class);
    GameState gameState = mock(GameState.class);
    entity1 = mock(Entity.class);
    entity2 = mock(Entity.class);
    context = mock(CollisionContextRecord.class);
    strategy = new ConsumeStrategy();

    when(context.entity1()).thenReturn(entity1);
    when(context.entity2()).thenReturn(entity2);
    when(context.gameMap()).thenReturn(gameMap);
    when(context.gameState()).thenReturn(gameState);
  }

  @Test
  void handleCollision_entityExists_removesEntitySuccessfully() throws EntityNotFoundException {
    doNothing().when(gameMap).removeEntity(entity2);

    assertDoesNotThrow(() -> strategy.handleCollision(context));
    // asked chatGpt how to do this since we mocking map
    verify(gameMap).removeEntity(entity2);
  }

  @Test
  void handleCollision_entityDoesNotExist_throwsException() throws EntityNotFoundException {
    doThrow(new EntityNotFoundException("Not found"))
        .when(gameMap).removeEntity(entity2);

    assertThrows(EntityNotFoundException.class, () -> strategy.handleCollision(context));

    verify(gameMap).removeEntity(entity2);
  }
}
