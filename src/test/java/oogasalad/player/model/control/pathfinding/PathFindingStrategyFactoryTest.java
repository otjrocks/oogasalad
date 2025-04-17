package oogasalad.player.model.control.pathfinding;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.exceptions.PathFindingStrategyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathFindingStrategyFactoryTest {

  @Test
  void createPathFindingStrategy_bfsStrategy_createsInstanceOfBfsPathFindingStrategy() {
    PathFindingStrategy strategy = PathFindingStrategyFactory.createPathFindingStrategy("Bfs");
    assertNotNull(strategy);
    assertInstanceOf(BfsPathFindingStrategy.class, strategy);
  }

  @Test
  void createPathFindingStrategy_euclideanStrategy_createsInstanceOfEuclideanPathFindingStrategy() {
    PathFindingStrategy strategy = PathFindingStrategyFactory.createPathFindingStrategy("Euclidean");
    assertNotNull(strategy);
    assertInstanceOf(EuclideanPathFindingStrategy.class, strategy);
  }

  @Test
  void createPathFindingStrategy_randomStrategy_createsInstanceOfRandomPathFindingStrategy() {
    PathFindingStrategy strategy = PathFindingStrategyFactory.createPathFindingStrategy("Random");
    assertNotNull(strategy);
    assertInstanceOf(RandomPathFindingStrategy.class, strategy);
  }

  @Test
  void createPathFindingStrategy_invalidStrategyName_throwsException() {
    PathFindingStrategyException exception = assertThrows(
        PathFindingStrategyException.class,
        () -> PathFindingStrategyFactory.createPathFindingStrategy("NonExistent")
    );
    assertTrue(exception.getMessage().contains("Failed to instantiate strategy"));
  }

  // Dummy class for testing no public constructor case
  public static class HiddenPathFindingStrategy implements PathFindingStrategy {
    private HiddenPathFindingStrategy() {}

    @Override
    public int[] getPath(GameMap map, int startX, int startY, int targetX, int targetY,
        EntityPlacement thisEntity, Direction thisDirection) {
      return new int[0];
    }
  }

  @Test
  void createPathFindingStrategy_noPublicConstructorStrategy_throwsException() {
    PathFindingStrategyException exception = assertThrows(
        PathFindingStrategyException.class,
        () -> PathFindingStrategyFactory.createPathFindingStrategy("Hidden")
    );
    assertTrue(exception.getMessage().contains("Failed to instantiate strategy"));
  }
}
