package oogasalad.engine.records.model;

import oogasalad.engine.records.config.model.ModeChangeInfo;

public record ModeChangeEventRecord(
    EntityTypeRecord entityType,
    ModeChangeInfo modeChangeInfo,
    ConditionRecord changeCondition
) { }
