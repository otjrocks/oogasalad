package oogasalad.engine.model.controlConfig.targetStrategy;

import oogasalad.engine.model.controlConfig.targetStrategy.TargetCalculationConfig;

public record TargetAheadOfEntityConfig(String targetType, int tilesAhead)
    implements TargetCalculationConfig { }