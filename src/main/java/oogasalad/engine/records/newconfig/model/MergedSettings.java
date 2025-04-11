package oogasalad.engine.records.newconfig.model;

public record MergedSettings(Double gameSpeed, Integer startingLives, Integer initialScore,
                       String scoreStrategy, String winCondition,
                             String edgePolicy, int width, int height) {

}