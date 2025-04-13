package oogasalad.player.model.control.pathfinding;

import java.util.List;
import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;

public class RandomPathFindingStrategy implements PathFindingStrategy {

  @Override
  public int[] getPath(GameMap map, int startX, int startY, int targetX, int targetY,
      EntityPlacement thisEntity, Direction thisDirection) {

    List<int[]> possibleDirections = PathFindingStrategyHelperMethods.getValidDirections(map, startX, startY, thisEntity,
        thisDirection);

    return possibleDirections.stream().mapToInt(i -> i[0]).toArray();
  }
}
