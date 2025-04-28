package oogasalad.player.model.strategies.collision;

import static org.mockito.Mockito.*;
import static oogasalad.engine.records.CollisionContextRecord.StrategyAppliesTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.GameState;

class ReturnToSpawnLocationStrategyTest {

  // Test wrote by Claude 3.5 Sonnet and IntelliJ code assist
  private ReturnToSpawnLocationStrategy strategy;
  private Entity mockEntity1;
  private Entity mockEntity2;
  private EntityPlacement mockPlacement1;
  private EntityPlacement mockPlacement2;
  private GameMapInterface mockGameMap;
  private GameState mockGameState;

  private static final double INITIAL_X1 = 100.0;
  private static final double INITIAL_Y1 = 200.0;
  private static final double INITIAL_X2 = 300.0;
  private static final double INITIAL_Y2 = 400.0;
  private static final double CURRENT_X = 50.0;
  private static final double CURRENT_Y = 75.0;

  @BeforeEach
  void setUp() {
    strategy = new ReturnToSpawnLocationStrategy();
    mockEntity1 = mock(Entity.class);
    mockEntity2 = mock(Entity.class);
    mockPlacement1 = mock(EntityPlacement.class);
    mockPlacement2 = mock(EntityPlacement.class);
    mockGameMap = mock(GameMapInterface.class);
    mockGameState = mock(GameState.class);

    // Setup basic entity placements
    when(mockEntity1.getEntityPlacement()).thenReturn(mockPlacement1);
    when(mockEntity2.getEntityPlacement()).thenReturn(mockPlacement2);

    // Setup initial positions
    when(mockPlacement1.getInitialX()).thenReturn(INITIAL_X1);
    when(mockPlacement1.getInitialY()).thenReturn(INITIAL_Y1);
    when(mockPlacement2.getInitialX()).thenReturn(INITIAL_X2);
    when(mockPlacement2.getInitialY()).thenReturn(INITIAL_Y2);

    // Setup current positions
    when(mockPlacement1.getX()).thenReturn(CURRENT_X);
    when(mockPlacement1.getY()).thenReturn(CURRENT_Y);
    when(mockPlacement2.getX()).thenReturn(CURRENT_X);
    when(mockPlacement2.getY()).thenReturn(CURRENT_Y);
  }

  @Test
  void handleCollision_WhenApplyToEntity1_ReturnsEntity1ToSpawn() {
    CollisionContextRecord context = new CollisionContextRecord(
        mockEntity1,
        mockEntity2,
        mockGameMap,
        mockGameState,
        StrategyAppliesTo.ENTITY1
    );

    strategy.handleCollision(context);

    // Verify entity1 was returned to spawn
    verify(mockPlacement1).setX(INITIAL_X1);
    verify(mockPlacement1).setY(INITIAL_Y1);

    // Verify entity2 was not moved
    verify(mockPlacement2, never()).setX(anyDouble());
    verify(mockPlacement2, never()).setY(anyDouble());
  }

  @Test
  void handleCollision_WhenApplyToEntity2_ReturnsEntity2ToSpawn() {
    CollisionContextRecord context = new CollisionContextRecord(
        mockEntity1,
        mockEntity2,
        mockGameMap,
        mockGameState,
        StrategyAppliesTo.ENTITY2
    );

    strategy.handleCollision(context);

    // Verify entity2 was returned to spawn
    verify(mockPlacement2).setX(INITIAL_X2);
    verify(mockPlacement2).setY(INITIAL_Y2);

    // Verify entity1 was not moved
    verify(mockPlacement1, never()).setX(anyDouble());
    verify(mockPlacement1, never()).setY(anyDouble());
  }

  @Test
  void handleCollision_WithDifferentInitialPositions_ReturnsToCorrectPositions() {
    // Setup different initial positions
    double differentX1 = 150.0;
    double differentY1 = 250.0;
    when(mockPlacement1.getInitialX()).thenReturn(differentX1);
    when(mockPlacement1.getInitialY()).thenReturn(differentY1);

    CollisionContextRecord context = new CollisionContextRecord(
        mockEntity1,
        mockEntity2,
        mockGameMap,
        mockGameState,
        StrategyAppliesTo.ENTITY1
    );

    strategy.handleCollision(context);

    // Verify entity1 was returned to its specific spawn position
    verify(mockPlacement1).setX(differentX1);
    verify(mockPlacement1).setY(differentY1);
  }
}
