package oogasalad.engine.records.config.model;

public record MergedSettingsRecord(Double gameSpeed, Integer startingLives, Integer initialScore,
                                   String winCondition,
                                   String edgePolicy, int width, int height) {

}