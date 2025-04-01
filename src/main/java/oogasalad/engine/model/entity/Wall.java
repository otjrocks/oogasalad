package oogasalad.engine.model.entity;

import java.util.List;
import oogasalad.engine.model.EntityData;
import oogasalad.player.model.movement.Grid;


public class Wall extends Entity {
  private int myCurrentStep;

  public Wall(EntityData entityData) {
    super(entityData);
  }

  @Override
  public void update() {
    //Static Entity so update is empty
  }

}
