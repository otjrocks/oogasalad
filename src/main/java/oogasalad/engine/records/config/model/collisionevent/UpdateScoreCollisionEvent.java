package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEvent;

/**
 * A record containing information about an update score collision event.
 *
 * @param amount Amount to update the score by.
 * @author Owen Jennings
 */
public record UpdateScoreCollisionEvent(int amount) implements CollisionEvent {

}
