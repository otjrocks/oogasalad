package oogasalad.engine.model;

/**
 * A record to store information about a game.
 *
 * @param gameSpeed     A double representing the game speed.
 * @param startingLives The number of starting lives for a game.
 * @param initialScore  The initial score of a player.
 * @param scoreStrategy The edge policy to use for the game.
 * @param winCondition  The win condition object (EntityBased, ScoreBased, or SurviveForTime).
 * @param condition
 * @author Owen Jennings, Will He, Angela Predolac
 */
public record GameSettings(double gameSpeed,
                           int startingLives,
                           int initialScore,
                           String scoreStrategy,
                           Object winCondition,
                           oogasalad.engine.records.config.model.wincondition.WinCondition condition) {

}
