package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEventInterface;

/**
 * A record containing information about an update score collision event.
 *
 * @param amount Amount to update the score by.
 * @author Owen Jennings
 */
public record UpdateScoreCollisionEventRecord(int amount) implements CollisionEventInterface {

}
