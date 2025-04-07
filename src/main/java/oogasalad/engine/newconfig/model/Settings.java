package oogasalad.engine.newconfig.model;

public record Settings(Double gameSpeed, Integer startingLives, Integer initialScore,
                       String scoreStrategy, String winCondition) {

}
