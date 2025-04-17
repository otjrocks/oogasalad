package oogasalad.player.model.control.pathfinding;

import java.util.List;
import java.util.Random;
import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMapInterface;

/**
 * The RandomPathFindingStrategy class implements the PathFindingStrategy interface
 * and provides a random pathfinding mechanism for entities in a game map. It selects
 * a random valid direction for the entity to move towards the target.
 * 
 * <p>This strategy is useful for scenarios where unpredictable or non-deterministic
 * movement is desired for entities.</p>
 * 
 * <p>Note: If no valid directions are available, the strategy will return a movement
 * of {0, 0}, indicating no movement.</p>
 * 
 * <p>Dependencies:
 * <ul>
 *   <li>PathFindingStrategyHelperMethods - Used to determine valid directions for movement.</li>
 *   <li>Random - Used to randomly select a valid direction.</li>
 * </ul>
 * </p>
 * 
 * @author Jessica Chen
 */
public class RandomPathFindingStrategy implements PathFindingStrategyInterface {

  private final Random random = new Random();

  @Override
  public int[] getPath(GameMapInterface map, int startX, int startY, int targetX, int targetY,
      EntityPlacement thisEntity, Direction thisDirection) {

    List<int[]> possibleDirections = PathFindingStrategyHelperMethods.getValidDirections(map, startX, startY, thisEntity,
        thisDirection);

    if (possibleDirections.isEmpty()) {
      return new int[]{0, 0};
    }

    int[] chosenDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));

    return new int[]{chosenDirection[0] - startX, chosenDirection[1] - startY};
  }
}
