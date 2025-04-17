package oogasalad.engine.records.config.model.losecondition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategy;

/**
 * An interface used to parse information about a lose condition from the configuration file.
 *
 * @author Austin Huang
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = LivesBasedCondition.class, name = "LivesBased")
})
public interface LoseCondition {

  /**
   * Convert lose condition from JSON type to a GameOutcomeStrategy and returns it.
   *
   * @return GameOutcomeStrategy
   */
  GameOutcomeStrategy toStrategy();
}
