package oogasalad.engine.records.model;

public record ModeChangeEventRecord(
    EntityTypeRecord entityType,
    String currentMode,
    String nextMode,
    ConditionRecord changeCondition
) { }
