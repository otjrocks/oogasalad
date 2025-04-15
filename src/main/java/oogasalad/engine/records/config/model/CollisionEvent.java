package oogasalad.engine.records.config.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import oogasalad.engine.records.config.model.collisionevent.ConsumeCollisionEvent;
import oogasalad.engine.records.config.model.collisionevent.ReturnToSpawnLocationCollisionEvent;
import oogasalad.engine.records.config.model.collisionevent.StopCollisionEvent;
import oogasalad.engine.records.config.model.collisionevent.UpdateLivesCollisionEvent;
import oogasalad.engine.records.config.model.collisionevent.UpdateScoreCollisionEvent;

/**
 * An interface used to parse the collision events that occur in the game.
 *
 * @author Owen Jennings
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StopCollisionEvent.class, name = "Stop"),
    @JsonSubTypes.Type(value = UpdateScoreCollisionEvent.class, name = "UpdateScore"),
    @JsonSubTypes.Type(value = ConsumeCollisionEvent.class, name = "Consume"),
    @JsonSubTypes.Type(value = UpdateLivesCollisionEvent.class, name = "UpdateLives"),
    @JsonSubTypes.Type(value = ReturnToSpawnLocationCollisionEvent.class, name = "ReturnToSpawnLocation")
})
public interface CollisionEvent {

}
