package oogasalad.player.model.movement.targetcalculation;

/**
 * Interface representing a strategy for calculating target positions.
 * Implementations of this interface define how to determine the X and Y
 * coordinates of a target position.
 */
public interface TargetStrategy {

  /**
   * Calculates and returns the X-coordinate of the target position.
   *
   * @return the X-coordinate of the target position
   */
  int getTargetPositionX();

  /**
   * Calculates and returns the Y-coordinate of the target position.
   *
   * @return the Y-coordinate of the target position
   */
  int getTargetPositionY();
}
