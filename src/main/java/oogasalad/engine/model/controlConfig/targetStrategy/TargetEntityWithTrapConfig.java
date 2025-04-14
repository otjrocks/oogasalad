package oogasalad.engine.model.controlConfig.targetStrategy;

import oogasalad.engine.model.controlConfig.targetStrategy.TargetCalculationConfig;

public record TargetEntityWithTrapConfig(String targetType, int tilesAhead, String teamEntityType)
    implements TargetCalculationConfig { }