package oogasalad.engine.model.controlConfig;

import oogasalad.engine.model.controlConfig.targetStrategy.TargetCalculationConfigInterface;

public record TargetControlConfigRecord(
    String pathFindingStrategy,
    TargetCalculationConfigInterface targetCalculationConfig
) implements ControlConfigInterface { }
