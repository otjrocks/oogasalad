package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEvent;

/**
 * A record storing information about an update lives collision event from the configuration file.
 *
 * @param amount The amount of update lives by.
 */
public record UpdateLivesCollisionEvent(int amount) implements CollisionEvent {

}
