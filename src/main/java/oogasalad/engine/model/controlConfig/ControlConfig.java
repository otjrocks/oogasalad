package oogasalad.engine.model.controlConfig;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import oogasalad.player.model.control.NoneControlStrategy;

/**
 * An interface that defines how to convert JSON objects into the correct corresponding java
 * records.
 *
 * @author Owen Jennings
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "controlStrategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = NoneControlConfig.class, name = "None"),
    @JsonSubTypes.Type(value = KeyboardControlConfig.class, name = "Keyboard"),
    @JsonSubTypes.Type(value = TargetControlConfig.class, name = "Target"),
    @JsonSubTypes.Type(value = ConditionalControlConfig.class, name = "Conditional")
})
public interface ControlConfig {

}
