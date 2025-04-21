package oogasalad.engine.records.config.model;

import java.util.List;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;

public record EntityPropertiesRecord(
    String name,
    Double movementSpeed,
    List<String> blocks
) { }
