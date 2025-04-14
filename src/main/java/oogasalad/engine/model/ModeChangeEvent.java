package oogasalad.engine.model;

public record ModeChangeEvent(
    EntityType entityType,
    String currentMode,
    String nextMode,
    Condition changeCondition
) { }
