package oogasalad.engine.model.api;

import oogasalad.engine.model.strategies.gameoutcome.EntityBasedOutcomeStrategy;
import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategy;
import oogasalad.engine.model.strategies.gameoutcome.ScoreBasedOutcomeStrategy;
import oogasalad.engine.records.config.model.losecondition.LivesBasedCondition;
import oogasalad.engine.records.config.model.losecondition.LoseCondition;
import oogasalad.engine.records.config.model.wincondition.EntityBasedCondition;
import oogasalad.engine.records.config.model.wincondition.SurviveForTimeCondition;
import oogasalad.engine.records.config.model.wincondition.WinCondition;

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
  public static GameOutcomeStrategy createWinStrategy(WinCondition winCondition) {
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
  public static GameOutcomeStrategy createLoseStrategy(LoseCondition loseCondition) {
    if (loseCondition == null) {
      throw new IllegalArgumentException("Lose condition cannot be null");
    }
    return loseCondition.toStrategy();
  }
}
