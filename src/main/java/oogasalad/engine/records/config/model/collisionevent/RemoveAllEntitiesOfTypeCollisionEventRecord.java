package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEventInterface;

public record RemoveAllEntitiesOfTypeCollisionEventRecord(String targetType) implements
    CollisionEventInterface {

}
