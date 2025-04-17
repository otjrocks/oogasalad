package oogasalad.engine.model;

public record ModeChangeEventRecord(
    EntityTypeRecord entityType,
    String currentMode,
    String nextMode,
    ConditionRecord changeCondition
) { }
