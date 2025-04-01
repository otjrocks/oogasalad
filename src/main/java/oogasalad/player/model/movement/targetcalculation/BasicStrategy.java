package oogasalad.player.model.movement.targetcalculation;


/**
 * BasicStrategy is a simple implementation of the TargetStrategy interface.
 * This strategy always returns a target position of (0, 0).
 * 
 * <p>It can be used as a default or placeholder strategy when no specific
 * targeting logic is required. Honestly, probably will be deleted.
 * 
 * @author Jessica Chen
 */
public class BasicStrategy implements TargetStrategy {

  @Override
  public int getTargetPositionX() {
    return 0;
  }

  @Override
  public int getTargetPositionY() {
    return 0;
  }
}
