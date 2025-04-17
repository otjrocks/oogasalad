package oogasalad.player.model.control;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.engine.model.controlConfig.ConditionalControlConfigRecord;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.control.pathfinding.PathFindingStrategyInterface;
import oogasalad.player.model.control.pathfinding.PathFindingStrategyFactory;
import oogasalad.player.model.control.targetcalculation.TargetStrategyInterface;
import oogasalad.player.model.control.targetcalculation.TargetStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

class ConditionalControlStrategyTest {

  private GameMapInterface mockMap;
  private EntityPlacement mockPlacement;
  private Entity mockEntity;
  private PathFindingStrategyInterface mockStrategyIn;
  private PathFindingStrategyInterface mockStrategyOut;
  private TargetStrategyInterface mockTargetStrategy;

  @BeforeEach
  void setup() {
    mockMap = mock(GameMapInterface.class);
    mockPlacement = mock(EntityPlacement.class);
    mockEntity = mock(Entity.class);
    mockStrategyIn = mock(PathFindingStrategyInterface.class);
    mockStrategyOut = mock(PathFindingStrategyInterface.class);
    mockTargetStrategy = mock(TargetStrategyInterface.class);
  }

  @Test
  void update_targetWithinRadius_usesInRadiusStrategy() {
    // Setup target 1 tile away
    when(mockPlacement.getX()).thenReturn(4.0);
    when(mockPlacement.getY()).thenReturn(4.0);
    when(mockEntity.getEntityDirection()).thenReturn(Direction.R);
    when(mockEntity.canMove(Direction.R)).thenReturn(true);
    when(mockTargetStrategy.getTargetPosition()).thenReturn(new int[]{5, 4});

    when(mockStrategyIn.getPath(mockMap, 4, 4, 5, 4, mockPlacement, Direction.R))
        .thenReturn(new int[]{1, 0});

    try (
        MockedStatic<TargetStrategyFactory> targetFactoryMock = mockStatic(TargetStrategyFactory.class);
        MockedStatic<PathFindingStrategyFactory> pathFactoryMock = mockStatic(PathFindingStrategyFactory.class)
    ) {
      targetFactoryMock.when(() -> TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap))
          .thenReturn(mockTargetStrategy);

      pathFactoryMock.when(() -> PathFindingStrategyFactory.createPathFindingStrategy("AStar"))
          .thenReturn(mockStrategyIn);
      pathFactoryMock.when(() -> PathFindingStrategyFactory.createPathFindingStrategy("Dijkstra"))
          .thenReturn(mockStrategyOut);

      var config = new ConditionalControlConfigRecord(5, "AStar", "Dijkstra", null);
      ConditionalControlStrategy strategy = new ConditionalControlStrategy(mockMap, mockPlacement, config);

      strategy.update(mockEntity);

      verify(mockEntity).setEntitySnapDirection(Direction.R);
    }
  }

  @Test
  void update_targetOutsideRadius_usesOutRadiusStrategy() {
    // Setup target far away
    when(mockPlacement.getX()).thenReturn(0.0);
    when(mockPlacement.getY()).thenReturn(0.0);
    when(mockEntity.getEntityDirection()).thenReturn(Direction.R);
    when(mockEntity.canMove(Direction.R)).thenReturn(true);
    when(mockTargetStrategy.getTargetPosition()).thenReturn(new int[]{10, 0});

    when(mockStrategyOut.getPath(mockMap, 0, 0, 10, 0, mockPlacement, Direction.R))
        .thenReturn(new int[]{1, 0});

    try (
        MockedStatic<TargetStrategyFactory> targetFactoryMock = mockStatic(TargetStrategyFactory.class);
        MockedStatic<PathFindingStrategyFactory> pathFactoryMock = mockStatic(PathFindingStrategyFactory.class)
    ) {
      targetFactoryMock.when(() -> TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap))
          .thenReturn(mockTargetStrategy);

      pathFactoryMock.when(() -> PathFindingStrategyFactory.createPathFindingStrategy("AStar"))
          .thenReturn(mockStrategyIn);
      pathFactoryMock.when(() -> PathFindingStrategyFactory.createPathFindingStrategy("Dijkstra"))
          .thenReturn(mockStrategyOut);

      var config = new ConditionalControlConfigRecord(3, "AStar", "Dijkstra", null);
      ConditionalControlStrategy strategy = new ConditionalControlStrategy(mockMap, mockPlacement, config);

      strategy.update(mockEntity);

      verify(mockEntity).setEntitySnapDirection(Direction.R);
    }
  }

  @Test
  void update_cannotMoveInDirection_doesNotSetDirection() {
    when(mockPlacement.getX()).thenReturn(2.0);
    when(mockPlacement.getY()).thenReturn(2.0);
    when(mockEntity.getEntityDirection()).thenReturn(Direction.D);
    when(mockEntity.canMove(Direction.D)).thenReturn(false);
    when(mockTargetStrategy.getTargetPosition()).thenReturn(new int[]{2, 4});

    when(mockStrategyIn.getPath(mockMap, 2, 2, 2, 4, mockPlacement, Direction.D))
        .thenReturn(new int[]{0, 1});

    try (
        MockedStatic<TargetStrategyFactory> targetFactoryMock = mockStatic(TargetStrategyFactory.class);
        MockedStatic<PathFindingStrategyFactory> pathFactoryMock = mockStatic(PathFindingStrategyFactory.class)
    ) {
      targetFactoryMock.when(() -> TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap))
          .thenReturn(mockTargetStrategy);

      pathFactoryMock.when(() -> PathFindingStrategyFactory.createPathFindingStrategy("AStar"))
          .thenReturn(mockStrategyIn);
      pathFactoryMock.when(() -> PathFindingStrategyFactory.createPathFindingStrategy("Dijkstra"))
          .thenReturn(mockStrategyOut);

      var config = new ConditionalControlConfigRecord(10, "AStar", "Dijkstra", null);
      ConditionalControlStrategy strategy = new ConditionalControlStrategy(mockMap, mockPlacement, config);

      strategy.update(mockEntity);

      verify(mockEntity, never()).setEntitySnapDirection(any());
    }
  }
}
