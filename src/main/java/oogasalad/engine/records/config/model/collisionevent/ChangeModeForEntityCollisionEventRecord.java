package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEventInterface;

public record ChangeModeForEntityCollisionEventRecord(int duration, String newMode) implements
    CollisionEventInterface {

}
