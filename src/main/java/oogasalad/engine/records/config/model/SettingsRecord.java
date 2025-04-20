package oogasalad.engine.records.config.model;

import oogasalad.engine.records.config.model.wincondition.WinConditionInterface;
import oogasalad.engine.records.config.model.losecondition.LivesBasedConditionRecord;

/**
 * A record that encapsulates global game settings.
 *
 * @param gameSpeed     The speed factor for the game (e.g., 1.0 is normal speed)
 * @param startingLives The number of lives the player starts with
 * @param initialScore  The score the player starts with
 * @param scoreStrategy The strategy for calculating scores (e.g., "Cumulative", "HighestLevel")
 * @param winCondition  The condition that determines when a level is won
 * @param loseCondition The condition that determines when the game is lost
 * @author angelapredolac
 */
public record SettingsRecord(
        double gameSpeed,
        int startingLives,
        int initialScore,
        String scoreStrategy,
        WinConditionInterface winCondition,
        LivesBasedConditionRecord loseCondition
) {
}
