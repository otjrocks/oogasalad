package oogasalad.engine.records.newconfig.model.collisionevent;

/**
 * A record storing information about an update lives collision event from the configuration file.
 *
 * @param amount The amount of update lives by.
 */
public record UpdateLivesCollisionEvent(int amount) implements CollisionEvent {

}
