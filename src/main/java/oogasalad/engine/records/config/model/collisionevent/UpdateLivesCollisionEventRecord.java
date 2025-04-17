package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEventInterface;

/**
 * A record storing information about an update lives collision event from the configuration file.
 *
 * @param amount The amount of update lives by.
 */
public record UpdateLivesCollisionEventRecord(int amount) implements CollisionEventInterface {

}
