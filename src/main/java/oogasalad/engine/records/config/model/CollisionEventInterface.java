package oogasalad.engine.records.config.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import oogasalad.engine.records.config.model.collisionevent.*;

/**
 * An interface used to parse the collision events that occur in the game.
 *
 * @author Owen Jennings
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StopCollisionEventRecord.class, name = "Stop"),
    @JsonSubTypes.Type(value = UpdateScoreCollisionEventRecord.class, name = "UpdateScore"),
    @JsonSubTypes.Type(value = ConsumeCollisionEventRecord.class, name = "Consume"),
    @JsonSubTypes.Type(value = UpdateLivesCollisionEventRecord.class, name = "UpdateLives"),
    @JsonSubTypes.Type(value = ReturnToSpawnLocationCollisionEventRecord.class, name = "ReturnToSpawnLocation"),
    @JsonSubTypes.Type(value = ChangeModeForTypeCollisionEventRecord.class, name = "ChangeModeForType"),
    @JsonSubTypes.Type(value = ResetTimeElapsedCollisionEventRecord.class, name = "ResetTimeElapsed"),
    @JsonSubTypes.Type(value = RemoveAllEntitiesOfTypeCollisionEventRecord.class, name = "RemoveAllEntitiesOfType"),
    @JsonSubTypes.Type(value = TemporaryModeChangeCollisionEventRecord.class, name = "TemporaryModeChange"),
    @JsonSubTypes.Type(value = ChangeModeForEntityCollisionEventRecord.class, name = "ChangeModeForEntity"),
    @JsonSubTypes.Type(value = AddTailCollisionEventRecord.class, name = "AddTail"),
    @JsonSubTypes.Type(value = TeleportToPartnerCollisionEventRecord.class, name = "TeleportToPartner")
})
public interface CollisionEventInterface {

}
