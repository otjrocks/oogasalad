package oogasalad.player.model.movement.targetcalculation;


import java.util.Iterator;
import java.util.Optional;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;

/**
 * The TargetTypeStrategy class implements the TargetStrategy interface and provides a mechanism to
 * calculate the target position on a game map based on a specified target type and a number of
 * tiles ahead. This strategy is useful for determining the position of a specific type of entity
 * (e.g., player, enemy, object) relative to the current position.
 *
 * <p>The class uses the direction of the entity and its current placement on the game map to
 * calculate the target position. If no entity of the specified type is found, the default behavior
 * is to return a position of (0, 0), which can be modified later to support random movement or
 * other fallback strategies.</p>
 *
 * <p>Key features of this class include:</p>
 * <ul>
 *   <li>Ability to specify the type of target entity to locate.</li>
 *   <li>Calculation of the target position based on the entity's direction and a specified
 *       number of tiles ahead.</li>
 *   <li>Support for optional fallback behavior when no target entity is found.</li>
 * </ul>
 *
 * <p>This class is designed to work with the GameMap and Entity classes, which provide the
 * necessary data for target calculation.</p>
 *
 * @author Jessica Chen
 */
public class TargetTypeStrategy implements TargetStrategy {

  private final GameMap myGameMap;
  private final String myTargetType;
  private final int myTilesAhead;

  /**
   * Constructs a TargetTypeStrategy object that determines the target type and calculates the
   * target position based on the specified number of tiles ahead on the game map.
   *
   * @param gameMap    the game map on which the target calculation is performed
   * @param targetType the type of target to calculate (e.g., player, enemy, object)
   * @param tilesAhead the number of tiles ahead from the current position to calculate the target
   */
  public TargetTypeStrategy(GameMap gameMap, String targetType, int tilesAhead) {
    myGameMap = gameMap;
    myTargetType = targetType;
    myTilesAhead = tilesAhead;
  }


  @Override
  public int[] getTargetPosition() {
    Optional<Entity> entity = TargetStrategyHelperMethods.findFirstEntityOfType(myGameMap,
        myTargetType);

    // if no entity, no target so stay where you are
    // or random movement (design choice for later)
    return entity.map(value -> calcTargetPosition(value.getEntityDirection(),
        (int) value.getEntityPlacement().getX(),
        (int) value.getEntityPlacement().getY())).orElseGet(() -> new int[]{0, 0});

  }

  private int[] calcTargetPosition(char dir, int x, int y) {
    int targetX = x;
    int targetY = y;

    switch (dir) {

      case 'R':
        targetX += myTilesAhead;
        break;
      case 'D':
        targetY += myTilesAhead;
        break;
      case 'L':
        targetX -= myTilesAhead;
        break;
      default: // default up
        targetY -= myTilesAhead;
        break;
    }
    return new int[]{targetX, targetY};
  }
}
