package oogasalad.engine.utility.constants;

/**
 * Represents the status of the game when the game loop is paused or ended.
 *
 * <p>This enum is used to communicate the reason for stopping or pausing the game loop
 * and to trigger appropriate UI or control logic based on the outcome of gameplay.</p>
 */
public enum GameEndStatus {
  WIN,
  LOSS,
  PAUSE_ONLY
}
