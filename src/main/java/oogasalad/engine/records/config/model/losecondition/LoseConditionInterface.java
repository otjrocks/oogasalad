package oogasalad.engine.records.config.model.losecondition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Optional;
import oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface;

/**
 * An interface used to parse information about a lose condition from the configuration file.
 *
 * @author Austin Huang
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = LivesBasedConditionRecord.class, name = "LivesBased"),
    @JsonSubTypes.Type(value = TimeBasedConditionRecord.class, name = "TimeBased"),

})
public interface LoseConditionInterface {

  /**
   * Convert lose condition from JSON type to a GameOutcomeStrategy and returns it.
   *
   * @return GameOutcomeStrategy
   */
  default GameOutcomeStrategyInterface toStrategy() {
    throw new UnsupportedOperationException("toStrategy not implemented for: "
        + this.getClass().getSimpleName());
  }

  /**
   * Retrieves the type of the type condition. This method provides a default implementation that
   * returns "LivesBased".
   *
   * @return a string representing the type of the win condition
   */
  default String getConditionType() {
    return "LivesBased"; // Default fallback
  }


  /**
   * Retrieves the value associated with the loss condition, if any.
   *
   * @return an {@code Optional} containing the condition value if present, or an empty
   * {@code Optional} if no value is set.
   */
  default Optional<String> getConditionValue() {
    return Optional.empty();
  }

}
