package oogasalad.engine.records.model;

/**
 * A record to store information about a game.
 *
 * @param gameSpeed     A double representing the game speed.
 * @param startingLives The number of starting lives for a game.
 * @param initialScore  The initial score of a player.
 * @author Owen Jennings, Will He
 */
public record GameSettingsRecord(double gameSpeed,
                                 int startingLives,
                                 int initialScore) {

}
