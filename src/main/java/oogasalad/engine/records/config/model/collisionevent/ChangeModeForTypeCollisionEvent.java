package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEvent;

public record ChangeModeForTypeCollisionEvent(String entityType, String newMode) implements CollisionEvent {
}
