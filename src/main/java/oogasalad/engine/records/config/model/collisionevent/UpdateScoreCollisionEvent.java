package oogasalad.engine.records.config.model.collisionevent;

/**
 * A record containing information about an update score collision event.
 *
 * @param amount Amount to update the score by.
 * @author Owen Jennings
 */
public record UpdateScoreCollisionEvent(int amount) implements CollisionEvent {

}
