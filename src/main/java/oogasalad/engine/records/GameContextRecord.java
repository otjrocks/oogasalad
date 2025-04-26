package oogasalad.engine.records;

import oogasalad.player.controller.GameInputManager;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.GameStateInterface;

/**
 * A record representing the context of the current game environment, including the overall game
 * state and the game map.
 *
 * @param gameMap   the map representing the current layout and all entities within the game
 * @param gameState the current state of the game, including score, status, and other global data
 */
public record GameContextRecord(GameInputManager inputManager, GameMapInterface gameMap, GameStateInterface gameState) {

  public GameContextRecord {
    if (gameMap == null || gameState == null) {
      throw new IllegalArgumentException("GameContext can't contain null values.");
    }
  }
}
