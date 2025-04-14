package oogasalad.engine.model.controlConfig.targetStrategy;

public record TargetAheadOfEntityConfig(String targetType, int tilesAhead)
    implements TargetCalculationConfig { }