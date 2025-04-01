package oogasalad.engine.model.entity;


import oogasalad.engine.model.EntityData;
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
   * @param entityData The data to include with this entity.
   * @param gameMap    The game map this entity is a part of.
   */
  public BfsEntity(EntityData entityData, GameMap gameMap) {
    super(entityData);

    myGameMap = gameMap;
    myPathFindingStrategy = new BfsPathFindingStrategy();
  }

  @Override
  public void update() {
    int[] dir = myPathFindingStrategy.getPath(myGameMap, (int) getEntityData().getInitialX(),
        (int) getEntityData().getInitialY(), 0, 0);

    getEntityData().setInitialX(getEntityData().getInitialX() + dir[0]);
    getEntityData().setInitialY(getEntityData().getInitialY() + dir[1]);
  }

}
