package oogasalad.engine.records.config.model.wincondition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Optional;
import oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface;

/**
 * An interface used to parse information about a win condition from the configuration file.
 *
 * @author Owen Jennings
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SurviveForTimeConditionRecord.class, name = "SurviveForTime"),
    @JsonSubTypes.Type(value = EntityBasedConditionRecord.class, name = "EntityBased")
})
public interface WinConditionInterface {

  /**
   * Retrieves the amount of time associated with the win condition, if applicable.
   * By default, this method returns an empty {@link Optional}, indicating that no time amount is specified.
   *
   * @return an {@link Optional} containing the time amount if specified, or an empty {@link Optional} otherwise.
   */
  @JsonIgnore
  default Optional<Integer> getTimeAmount() {
    return Optional.empty();
  }

  /**
   * Retrieves the type of entity associated with the win condition, if applicable.
   * 
   * @return an {@link Optional} containing the entity type as a {@link String}, or an empty {@link Optional} if no entity type is specified.
   */
  @JsonIgnore
  default Optional<String> getEntityType() {
    return Optional.empty();
  }

  /**
   * Convert win condition from JSON type to a GameOutcomeStrategy and returns it.
   *
   * @return GameOutcomeStrategy
   */
  default GameOutcomeStrategyInterface toStrategy() {
    throw new UnsupportedOperationException("toStrategy not implemented for: "
        + this.getClass().getSimpleName());
  };
}
