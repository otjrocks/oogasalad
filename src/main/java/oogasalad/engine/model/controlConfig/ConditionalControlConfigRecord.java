package oogasalad.engine.model.controlConfig;

import oogasalad.engine.model.controlConfig.targetStrategy.TargetCalculationConfigInterface;

public record ConditionalControlConfigRecord(
    int radius,
    String pathFindingStrategyInRadius,
    String pathFindingStrategyOutRadius,
    TargetCalculationConfigInterface targetCalculationConfig
) implements ControlConfigInterface { }
