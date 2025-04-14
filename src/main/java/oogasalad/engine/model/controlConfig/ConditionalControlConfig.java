package oogasalad.engine.model.controlConfig;

import oogasalad.engine.model.controlConfig.targetStrategy.TargetCalculationConfig;

public record ConditionalControlConfig(
    int radius,
    String pathFindingStrategyInRadius,
    String pathFindingStrategyOutRadius,
    TargetCalculationConfig targetCalculationConfig
) implements ControlConfig { }
