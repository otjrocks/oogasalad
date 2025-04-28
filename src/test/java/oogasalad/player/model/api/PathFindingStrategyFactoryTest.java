package oogasalad.player.model.api;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.exceptions.PathFindingStrategyException;
import oogasalad.player.model.strategies.control.pathfinding.BfsPathFindingStrategy;
import oogasalad.player.model.strategies.control.pathfinding.EuclideanPathFindingStrategy;
import oogasalad.player.model.strategies.control.pathfinding.PathFindingStrategyInterface;
import oogasalad.player.model.strategies.control.pathfinding.RandomPathFindingStrategy;
import org.junit.jupiter.api.Test;

public class PathFindingStrategyFactoryTest {

  @Test
  void createPathFindingStrategy_bfsStrategy_createsInstanceOfBfsPathFindingStrategy() {
    PathFindingStrategyInterface strategy = PathFindingStrategyFactory.createPathFindingStrategy(
        "Bfs");
    assertNotNull(strategy);
    assertInstanceOf(BfsPathFindingStrategy.class, strategy);
  }

  @Test
  void createPathFindingStrategy_euclideanStrategy_createsInstanceOfEuclideanPathFindingStrategy() {
    PathFindingStrategyInterface strategy = PathFindingStrategyFactory.createPathFindingStrategy(
        "Euclidean");
    assertNotNull(strategy);
    assertInstanceOf(EuclideanPathFindingStrategy.class, strategy);
  }

  @Test
  void createPathFindingStrategy_randomStrategy_createsInstanceOfRandomPathFindingStrategy() {
    PathFindingStrategyInterface strategy = PathFindingStrategyFactory.createPathFindingStrategy(
        "Random");
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
  public static class HiddenPathFindingStrategy implements PathFindingStrategyInterface {

    private HiddenPathFindingStrategy() {
    }

    @Override
    public int[] getPath(GameMapInterface map, int startX, int startY, int targetX, int targetY,
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
