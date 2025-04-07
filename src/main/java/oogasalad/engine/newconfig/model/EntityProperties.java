package oogasalad.engine.newconfig.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// so it ignores blocks during parsing
@JsonIgnoreProperties(ignoreUnknown = true)
public record EntityProperties(String name, ControlType controlType, Double movementSpeed) {

}

