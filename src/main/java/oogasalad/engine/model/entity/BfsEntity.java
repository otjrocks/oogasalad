package oogasalad.engine.model.entity;


import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.movement.pathfinding.BfsPathFindingStrategy;
import oogasalad.player.model.movement.pathfinding.PathFindingStrategy;

// quick entity to demonstrate BfsMovement, I will admit its very scuffed

/**
 * A BFS entity.
 *
 * @author Jessica Chen
 */
public class BfsEntity extends Entity {

  private final GameMap myGameMap;
  // TODO: would be nice if this was static, so each class don't need own for path finidng
  private final PathFindingStrategy myPathFindingStrategy;

  /**
   * Create a BFS entity.
   *
   * @param entityPlacement The data to include with this entity.
   * @param gameMap    The game map this entity is a part of.
   */
  public BfsEntity(EntityPlacement entityPlacement, GameMap gameMap) {
    super(entityPlacement);

    myGameMap = gameMap;
    myPathFindingStrategy = new BfsPathFindingStrategy();
  }

  @Override
  public void update() {
    int[] dir = myPathFindingStrategy.getPath(myGameMap, (int) getEntityPlacement().getX(),
        (int) getEntityPlacement().getY(), 0, 0);

    getEntityPlacement().setX(getEntityPlacement().getX() + dir[0]);
    getEntityPlacement().setY(getEntityPlacement().getY() + dir[1]);
  }

}
