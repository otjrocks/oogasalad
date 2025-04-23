package oogasalad.player.model.strategies.gameoutcome;

import oogasalad.engine.records.GameContextRecord;

public class TimeBasedOutcomeStrategy implements GameOutcomeStrategyInterface {

  private final int amount;

  public TimeBasedOutcomeStrategy(int amount) {
    this.amount = amount;
  }

  @Override
  public boolean hasGameEnded(GameContextRecord gameContext) {
    return gameContext.gameState().getTimeElapsed() >= amount;
  }

  @Override
  public String getGameOutcome(GameContextRecord gameContext) {
    return hasGameEnded(gameContext) ? "Game Over" : "Game ongoing";
  }
}
