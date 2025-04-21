package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEventInterface;

public record TemporaryModeChangeCollisionEventRecord(String entityType, String temporaryMode, String transitionMode, int duration, int transitionTime) implements CollisionEventInterface {
}
