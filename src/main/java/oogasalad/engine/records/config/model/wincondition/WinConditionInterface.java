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

  @JsonIgnore
  default Optional<Integer> getTimeAmount() {
    return Optional.empty();
  }

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
