package oogasalad.engine.model.controlConfig.targetStrategy;

public record TargetAheadOfEntityConfigRecord(String targetType, int tilesAhead)
    implements TargetCalculationConfigInterface { }