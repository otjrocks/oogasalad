package oogasalad.engine.records.config.model.controlConfig.targetStrategy;

public record TargetAheadOfEntityConfigRecord(String targetType, int tilesAhead)
    implements TargetCalculationConfigInterface {

}