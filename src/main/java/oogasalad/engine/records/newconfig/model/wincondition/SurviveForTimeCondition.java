package oogasalad.engine.records.newconfig.model.wincondition;

/**
 * A record that encapsulates information about the survive for time win condition.
 *
 * @param amount Amount of time the player needs to survive.
 */
public record SurviveForTimeCondition(int amount) implements WinCondition {

}
