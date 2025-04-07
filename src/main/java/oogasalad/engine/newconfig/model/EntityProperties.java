package oogasalad.engine.newconfig.model;

import java.util.List;

public record EntityProperties(String name, ControlType controlType, Double movementSpeed,
                               List<String> blocks) {

}

