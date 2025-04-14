package oogasalad.engine.model.api;

import oogasalad.engine.LoggingManager;
import oogasalad.engine.model.strategies.gameoutcome.EntityBasedOutcomeStrategy;
import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategy;
import oogasalad.engine.model.strategies.gameoutcome.LivesBasedOutcome;
import oogasalad.engine.model.strategies.gameoutcome.ScoreBasedOutcomeStrategy;
import oogasalad.engine.records.newconfig.model.wincondition.EntityBasedCondition;
import oogasalad.engine.records.newconfig.model.wincondition.SurviveForTimeCondition;
import oogasalad.engine.records.newconfig.model.wincondition.WinCondition;

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
  public static GameOutcomeStrategy create(WinCondition winCondition) {
    if (winCondition instanceof SurviveForTimeCondition(int amount)) {
      return new ScoreBasedOutcomeStrategy(amount);
    }

    if (winCondition instanceof EntityBasedCondition(String entityType, int amount)) {
      return new EntityBasedOutcomeStrategy(entityType, amount);
    }

    throw new IllegalArgumentException("Unknown win condition type: " + winCondition.getClass());
  }
}
