package oogasalad.engine.records.newconfig.model.collisionevent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
