package oogasalad.engine.records.config.model.controlConfig;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Optional;

/**
 * An interface that defines how to convert JSON objects into the correct corresponding java
 * records.
 *
 * @author Owen Jennings
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "controlStrategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = NoneControlConfigRecord.class, name = "None"),
    @JsonSubTypes.Type(value = KeyboardControlConfigRecord.class, name = "Keyboard"),
    @JsonSubTypes.Type(value = TargetControlConfigRecord.class, name = "Target"),
    @JsonSubTypes.Type(value = ConditionalControlConfigRecord.class, name = "Conditional")
})
public interface ControlConfigInterface {

  default Optional<String> getPathFindingStrategyInRadius() {
    return Optional.empty();
  }

  default Optional<String> getPathFindingStrategyOutRadius() {
    return Optional.empty();
  }

  default Optional<Integer> getRadius() {
    return Optional.empty();
  }

}
