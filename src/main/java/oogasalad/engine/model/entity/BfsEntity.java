package oogasalad.engine.model.entity;

import java.util.List;

import oogasalad.engine.model.EntityData;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.movement.pathfinding.BfsPathFindingStrategy;
import oogasalad.player.model.movement.pathfinding.PathFindingStrategy;

// quick entity to demonstrate BfsMovement, I will admit its very scuffed
public class BfsEntity extends Entity {
  private List<int[]> myPath;
  private int myCurrentStep;

  public BfsEntity(EntityData entityData, GameMap gameMap) {
    super(entityData);

    PathFindingStrategy pathFindingStrategy = new BfsPathFindingStrategy();
    myPath = pathFindingStrategy.getPath(gameMap, (int) getEntityData().getInitialX(), (int) getEntityData().getInitialY(), 0, 0);
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
