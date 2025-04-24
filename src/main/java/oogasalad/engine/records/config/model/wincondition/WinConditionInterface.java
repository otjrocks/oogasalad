package oogasalad.engine.records.config.model.wincondition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Optional;
import oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface;

/**
 * An interface used to parse information about a win condition from the configuration file.
 *
 * @author Owen Jennings
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SurviveForTimeConditionRecord.class, name = "SurviveForTime"),
    @JsonSubTypes.Type(value = EntityBasedConditionRecord.class, name = "EntityBased")
})
public interface WinConditionInterface {


  /**
   * Retrieves the type of the win condition.
   * This method provides a default implementation that returns "SurviveForTime".
   * 
   * @return a string representing the type of the win condition
   */
  default String getConditionType() {
    return "SurviveForTime"; // Default fallback
  }

  /**
   * Retrieves the value associated with the win condition, if any.
   * 
   * @return an {@code Optional} containing the condition value if present, or an empty {@code Optional} if no value is set.
   */
  default Optional<String> getConditionValue() {
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
