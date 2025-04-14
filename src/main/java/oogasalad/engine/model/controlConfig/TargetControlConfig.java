package oogasalad.engine.model.controlConfig;

import oogasalad.engine.model.controlConfig.targetStrategy.TargetCalculationConfig;

public record TargetControlConfig(
    String pathFindingStrategy,
    TargetCalculationConfig targetCalculationConfig
) implements ControlConfig { }
