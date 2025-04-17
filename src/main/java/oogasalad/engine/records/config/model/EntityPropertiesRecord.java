package oogasalad.engine.records.config.model;

import java.util.List;
import oogasalad.engine.model.controlConfig.ControlConfigInterface;

public record EntityPropertiesRecord(
    String name,
    ControlConfigInterface controlConfig,
    Double movementSpeed,
    List<String> blocks
) { }
