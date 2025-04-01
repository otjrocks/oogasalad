package oogasalad.engine.model.entity;

import java.util.List;

import javafx.scene.Scene;
import oogasalad.engine.model.EntityData;
import oogasalad.player.model.movement.Grid;
import oogasalad.player.model.movement.pathfinding.BfsPathFindingStrategy;
import oogasalad.player.model.movement.pathfinding.PathFindingStrategy;

// quick entity to demonstrate BfsMovement, I will admit its very scuffed
public class BfsEntity extends Entity {
  private List<int[]> myPath;
  private int myCurrentStep;

  public BfsEntity(EntityData entityData) {
    super(entityData);

    PathFindingStrategy pathFindingStrategy = new BfsPathFindingStrategy();
    myPath = pathFindingStrategy.getPath(new Grid(20, 20), (int) getEntityData().getInitialX(), (int) getEntityData().getInitialY(), 0, 0);
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
