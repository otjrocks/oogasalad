package oogasalad.engine.records.newconfig.model;

import java.util.List;
import oogasalad.engine.model.controlConfig.ControlConfig;

public record EntityProperties(
    String name,
    ControlConfig controlConfig,
    Double movementSpeed,
    List<String> blocks
) { }
