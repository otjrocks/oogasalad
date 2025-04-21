package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEventInterface;

public record ChangeModeForEntityCollisionEventRecord(String newMode, int duration) implements CollisionEventInterface {
}
