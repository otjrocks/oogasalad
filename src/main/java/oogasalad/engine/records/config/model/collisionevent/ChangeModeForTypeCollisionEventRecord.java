package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEventInterface;

public record ChangeModeForTypeCollisionEventRecord(String entityType, String newMode) implements
    CollisionEventInterface {

}
