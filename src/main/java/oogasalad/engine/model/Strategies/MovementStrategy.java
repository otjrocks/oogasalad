package oogasalad.engine.model.Strategies;

/**
 * An interface that defines how an entity moves.
 *
 * @author Austin Huang
 */
public interface MovementStrategy {

  /**
   * Compute the next position for an entity.
   */
  void computeNextPosition();

  /**
   * Update the position for an entity.
   */
  void updatePosition();
}
