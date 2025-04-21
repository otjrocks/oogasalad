package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEventInterface;

public record TemporaryModeChangeCollisionEventRecord(String entityType, String temporaryMode, int duration) implements CollisionEventInterface {
}
