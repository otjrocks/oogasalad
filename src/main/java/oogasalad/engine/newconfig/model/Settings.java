package oogasalad.engine.newconfig.model;

/**
 * Represents the configuration settings for a game. This record encapsulates various parameters
 * that define the initial state and behavior of the game.
 *
 * @param gameSpeed     The speed at which the game progresses, represented as a Double.
 * @param startingLives The number of lives the player starts with, represented as an Integer.
 * @param initialScore  The initial score of the player, represented as an Integer.
 * @param scoreStrategy The strategy used to calculate the score, represented as a String.
 * @param winCondition  The condition required to win the game, represented as a String.
 */
public record Settings(Double gameSpeed, Integer startingLives, Integer initialScore,
                       String scoreStrategy, String winCondition) {

}
