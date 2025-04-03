package oogasalad.player.model.movement.targetcalculation;

/**
 * Interface representing a strategy for calculating target positions.
 * Implementations of this interface define how to determine the X and Y
 * coordinates of a target position.
 */
public interface TargetStrategy {

  /**
   * Calculates and returns the coordinate of the target position.
   *
   * @return the coordinate of the target position given in [x, y]
   */
  int[] getTargetPosition();
}
