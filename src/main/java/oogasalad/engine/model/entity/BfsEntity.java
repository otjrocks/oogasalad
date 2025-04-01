package oogasalad.engine.model.entity;

import java.util.List;

import oogasalad.engine.model.EntityData;
import oogasalad.player.model.movement.Grid;
import oogasalad.player.model.movement.pathfinding.BfsPathFindingStrategy;
import oogasalad.player.model.movement.pathfinding.PathFindingStrategy;

// quick entity to demonstrate BfsMovement, I will admit its very scuffed

/**
 * A BFS entity.
 *
 * @author Jessica Chen
 */
public class BfsEntity extends Entity {

  private final List<int[]> myPath;
  private int myCurrentStep;

  /**
   * Create a BFS entity.
   *
   * @param entityData The data associated with this entity.
   */
  public BfsEntity(EntityData entityData) {
    super(entityData);

    PathFindingStrategy pathFindingStrategy = new BfsPathFindingStrategy();
    myPath = pathFindingStrategy.getPath(new Grid(20, 20), (int) getEntityData().getInitialX(),
        (int) getEntityData().getInitialY(), 0, 0);
  }

  @Override
  public void update() {
    if (myPath != null && myCurrentStep < myPath.size()) {
      getEntityData().setInitialX(myPath.get(myCurrentStep)[0]);
      getEntityData().setInitialY(myPath.get(myCurrentStep)[1]);
      myCurrentStep++;
    }
  }

}
