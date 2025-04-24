package oogasalad.engine.records.config.model.controlConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Optional;

/**
 * An interface that defines how to convert JSON objects into the correct corresponding java
 * records.
 *
 * @author Owen Jennings
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "controlStrategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = NoneControlConfigRecord.class, name = "None"),
    @JsonSubTypes.Type(value = KeyboardControlConfigRecord.class, name = "Keyboard"),
    @JsonSubTypes.Type(value = TargetControlConfigRecord.class, name = "Target"),
    @JsonSubTypes.Type(value = ConditionalControlConfigRecord.class, name = "Conditional")
})
public interface ControlConfigInterface {

  /**
   * Retrieves the pathfinding strategy, if available.
   * This method provides an optional string representing the strategy.
   *
   * @return an {@code Optional<String>} containing the pathfinding strategy
   *         within a radius, or {@code Optional.empty()} if no strategy is defined.
   */
  default Optional<String> getPathFindingStrategy() {
    return Optional.empty();
  }

  /**
   * Retrieves the pathfinding strategy within a certain radius, if available.
   * This method provides an optional string representing the strategy.
   *
   * @return an {@code Optional<String>} containing the pathfinding strategy
   *         within a radius, or {@code Optional.empty()} if no strategy is defined.
   */
  default Optional<String> getPathFindingStrategyInRadius() {
    return Optional.empty();
  }

  /**
   * Retrieves the pathfinding strategy for out-of-radius scenarios.
   * This method provides an optional string representation of the strategy.
   * By default, it returns an empty {@link Optional}.
   *
   * @return an {@link Optional} containing the pathfinding strategy for out-of-radius scenarios,
   *         or an empty {@link Optional} if no strategy is defined.
   */
  default Optional<String> getPathFindingStrategyOutRadius() {
    return Optional.empty();
  }

  /**
   * Retrieves the radius value associated with the control configuration.
   * By default, this method returns an empty {@link Optional}, indicating
   * that no radius value is specified.
   *
   * @return an {@link Optional} containing the radius value if specified,
   *         or an empty {@link Optional} if not.
   */
  default Optional<Integer> getRadius() {
    return Optional.empty();
  }

}
