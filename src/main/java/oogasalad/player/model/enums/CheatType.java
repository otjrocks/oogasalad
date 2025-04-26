package oogasalad.player.model.enums;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.player.controller.GameLoopController;
import oogasalad.player.controller.GameInputManager;

public enum CheatType {

  ADD_LIFE {
    @Override
    public void execute(GameInputManager inputManager, GameContextRecord context, GameLoopController loop) {
      if (inputManager.shouldAddLife()) {
        context.gameState().setLives(context.gameState().getLives() + 1);
      }
    }
  },
  PAUSE_GAME {
    @Override
    public void execute(GameInputManager inputManager, GameContextRecord context, GameLoopController loop) {
      if (inputManager.shouldPauseGame()) {
        loop.pauseGame();
      }
    }
  },
  NEXT_LEVEL {
    @Override
    public void execute(GameInputManager inputManager, GameContextRecord context, GameLoopController loop) {
      if (inputManager.shouldGoToNextLevel()) {
        context.inputManager().getGameScreenView().getGamePlayerView().handleNextLevel();
      }
    }
  },
  RESET_GAME {
    @Override
    public void execute(GameInputManager inputManager, GameContextRecord context, GameLoopController loop) {
      if (inputManager.shouldResetGame()) {
        context.inputManager().getGameScreenView().getGamePlayerView().handleResetGame();
      }
    }
  },
  SPEED_UP {
    @Override
    public void execute(GameInputManager inputManager, GameContextRecord context, GameLoopController loop) {
      if (inputManager.shouldSpeedUpGame()) {
        loop.setMyGameSpeedMultiplier(loop.getMyGameSpeedMultiplier() * 1.1);
      }
    }
  };

  public abstract void execute(GameInputManager inputManager, GameContextRecord context, GameLoopController loop);
}
