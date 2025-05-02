package oogasalad.player.model.enums;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.player.controller.GameLoopController;
import oogasalad.player.controller.GameInputManager;

/**
 * Represents different types of cheat codes available in the game. Each cheat type defines an
 * action that can be triggered via user input.
 * <p>
 * Each cheat type must implement the
 * {@link #execute(GameInputManager, GameContextRecord, GameLoopController)} method, which checks if
 * the relevant key is pressed and applies the cheat effect accordingly.
 * </p>
 *
 * @author Will
 */
public enum CheatType {

  /**
   * Cheat to add an extra life to the player's life count.
   */
  ADD_LIFE {
    @Override
    public void execute(GameInputManager inputManager, GameContextRecord context,
        GameLoopController loop) {
      if (inputManager.shouldAddLife()) {
        context.gameState().setLives(context.gameState().getLives() + 1);
      }
    }
  },

  /**
   * Cheat to pause the game loop.
   */
  PAUSE_GAME {
    @Override
    public void execute(GameInputManager inputManager, GameContextRecord context,
        GameLoopController loop) {
      if (inputManager.shouldPauseGame()) {
        loop.pauseGame();
      }
    }
  },

  /**
   * Cheat to immediately move to the next level.
   */
  NEXT_LEVEL {
    @Override
    public void execute(GameInputManager inputManager, GameContextRecord context,
        GameLoopController loop) {
      if (inputManager.shouldGoToNextLevel()) {
        context.inputManager().getGameScreenView().getGamePlayerView().handleNextLevel();
      }
    }
  },

  /**
   * Cheat to reset the game back to the first level.
   */
  RESET_GAME {
    @Override
    public void execute(GameInputManager inputManager, GameContextRecord context,
        GameLoopController loop) {
      if (inputManager.shouldResetGame()) {
        context.inputManager().getGameScreenView().getGamePlayerView().handleResetGame();
      }
    }
  },

  /**
   * Cheat to increase the game's speed by 10%.
   */
  SPEED_UP {
    @Override
    public void execute(GameInputManager inputManager, GameContextRecord context,
        GameLoopController loop) {
      if (inputManager.shouldSpeedUpGame()) {
        loop.setGameSpeedMultiplier(loop.getGameSpeedMultiplier() * 1.1);
      }
    }
  };

  /**
   * Executes the cheat behavior if the corresponding user input is active.
   *
   * @param inputManager the {@link GameInputManager} handling user inputs
   * @param context      the {@link GameContextRecord} containing the game state and map
   * @param loop         the {@link GameLoopController} controlling the game loop
   */
  public abstract void execute(GameInputManager inputManager, GameContextRecord context,
      GameLoopController loop);
}
