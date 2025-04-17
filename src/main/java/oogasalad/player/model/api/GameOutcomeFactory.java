package oogasalad.player.model.api;

import oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface;
import oogasalad.engine.records.config.model.losecondition.LoseConditionInterface;
import oogasalad.engine.records.config.model.wincondition.WinConditionInterface;

/**
 * A factory design pattern to create the appropriate GameOutcome strategy
 *
 * @author Luke Fu
 */
public class GameOutcomeFactory {

  /**
   * Creates the appropriate GameOutcomeStrategy based on the winCondition string.
   * Examples:
   * - "LivesBased()" → LivesBasedOutcome
   * - "EntityBased(dot)" → EntityBasedOutcomeStrategy("dot")
   * - "ScoreBased(1000)" → ScoreBasedOutcomeStrategy(1000)
   *
   * @param winCondition the WinCondition object
   * @return corresponding GameOutcomeStrategy
   */
  public static GameOutcomeStrategyInterface createWinStrategy(WinConditionInterface winCondition) {
    if (winCondition == null) {
      throw new IllegalArgumentException("Win condition cannot be null");
    }

    return winCondition.toStrategy();
  }

  /**
   * Creates the appropriate GameOutcomeStrategy based on the LoseCondition string.
   *
   * @param loseCondition the loseCondition object
   * @return corresponding GameOutcomeStrategy
   */
  public static GameOutcomeStrategyInterface createLoseStrategy(LoseConditionInterface loseCondition) {
    if (loseCondition == null) {
      throw new IllegalArgumentException("Lose condition cannot be null");
    }
    return loseCondition.toStrategy();
  }
}
