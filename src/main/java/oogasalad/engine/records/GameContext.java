package oogasalad.engine.records;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;

/**
 * A record representing the context of the current game environment, including
 * the overall game state and the game map.
 *
 * <p>This record is used to bundle together essential game data for processing logic,
 * such as strategies, rules, and other engine components that require access to both
 * the current {@link GameState} and {@link GameMap}.</p>
 *
 * <p>The constructor performs null-check validation to ensure that neither component
 * of the context is null at creation time.</p>
 *
 * @param gameState the current state of the game, including score, status, and other global data
 * @param gameMap the map representing the current layout and all entities within the game
 *
 * @see GameMap
 * @see GameState
 */
public record GameContext(GameState gameState, GameMap gameMap) {
  public GameContext {
    if (gameMap == null || gameState == null) {
      throw new IllegalArgumentException("GameContext can't contain null values.");
    }
  }
}
