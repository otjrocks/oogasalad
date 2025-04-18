package oogasalad.player.model.strategies.collision;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import oogasalad.engine.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.GameStateInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsumeStrategyTest {

  private GameMapInterface gameMap;
  private Entity entity2;
  private CollisionContextRecord context;
  private ConsumeStrategy strategy;

  @BeforeEach
  void setUp() {
    gameMap = mock(GameMapInterface.class);
    GameStateInterface gameState = mock(GameStateInterface.class);
    Entity entity1 = mock(Entity.class);
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
