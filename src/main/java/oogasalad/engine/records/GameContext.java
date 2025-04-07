package oogasalad.engine.records;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;

/**
 * A record representing the context of the current game environment, including
 * the overall game state and the game map.
 *
 * <p>This record is used to bundle together essential game data for processing logic,
 * such as strategies, rules, and other engine components that require access to both
 * the current {@link GameMap} and {@link GameState}.</p>
 *
 * <p>The constructor performs null-check validation to ensure that neither component
 * of the context is null at creation time.</p>
 *
 * @param gameMap the map representing the current layout and all entities within the game
 * @param gameState the current state of the game, including score, status, and other global data
 *
 * @see GameMap
 * @see GameState
 */
public record GameContext(GameMap gameMap, GameState gameState) {
  public GameContext {
    if (gameMap == null || gameState == null) {
      throw new IllegalArgumentException("GameContext can't contain null values.");
    }
  }
}
