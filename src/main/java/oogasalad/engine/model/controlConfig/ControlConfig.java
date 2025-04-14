package oogasalad.engine.model.controlConfig;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "controlStrategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = KeyboardControlConfig.class, name = "Keyboard"),
    @JsonSubTypes.Type(value = TargetControlConfig.class, name = "Target"),
    @JsonSubTypes.Type(value = ConditionalControlConfig.class, name = "Conditional")
})
public interface ControlConfig { }
