package oogasalad.engine.model;

/**
 * A record to store information about a game.
 *
 * @param gameSpeed     A double representing the game speed.
 * @param startingLives The number of starting lives for a game.
 * @param initialScore  The initial score of a player.
 * @param edgePolicy    The edge policy to use for the game.
 * @param width         The width of game window in "cells."
 * @param height        The height of the game in "cells."
 */
public record GameSettings(double gameSpeed,
                           int startingLives,
                           int initialScore,
                           String edgePolicy,
                           int width,
                           int height) {

}
