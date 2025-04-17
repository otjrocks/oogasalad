package oogasalad.engine.model.controlConfig.targetStrategy;

public record TargetEntityWithTrapConfigRecord(String targetType, int tilesAhead, String teamEntityType)
    implements TargetCalculationConfigInterface { }