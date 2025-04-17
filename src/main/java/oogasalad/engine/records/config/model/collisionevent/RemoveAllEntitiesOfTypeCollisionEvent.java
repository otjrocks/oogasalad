package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEvent;

public record RemoveAllEntitiesOfTypeCollisionEvent(String targetType) implements CollisionEvent {

}
