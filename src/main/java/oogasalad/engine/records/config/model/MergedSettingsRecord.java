package oogasalad.engine.records.config.model;

public record MergedSettingsRecord(Double gameSpeed, Integer startingLives, Integer initialScore,
                                   String scoreStrategy, String winCondition,
                                   String edgePolicy, int width, int height) {

}