package oogasalad.engine.model.strategies.gameoutcome;

import oogasalad.engine.model.GameStateInterface;
import oogasalad.engine.records.GameContextRecord;

/**
 * The {@code EntityBasedOutcomeStrategy} class implements the {@link GameOutcomeStrategyInterface} interface
 * to determine whether the game has ended based on the entities present in the game state, such as
 * remaining pellets.
 * <p>
 * This strategy typically checks if all necessary entities (e.g., pellets) have been consumed to
 * declare a victory.
 * </p>
 *
 * @author Austin Huang
 */
public class EntityBasedOutcomeStrategy implements GameOutcomeStrategyInterface {

  private final String entityType;
  /**
   * Constructs an {@code EntityBasedOutcomeStrategy} that determines the end of the game based on
   * the remaining count of a specific entity type in the game map.
   *
   * @param entityType the type of entity to track for game completion must match the type used
   *                   within the entity placement
   */
  public EntityBasedOutcomeStrategy(String entityType) {
    this.entityType = entityType;
  }

  /**
   * Determines if the game has ended based on the current {@link GameStateInterface}. Check conditions such
   * as whether all pellets have been consumed.
   *
   * @param context contains gameScore and gameMap
   * @return {@code true} if the game has ended, {@code false} otherwise
   */
  @Override
  public boolean hasGameEnded(GameContextRecord context) {
    return context.gameMap().getEntityCount(entityType) <= 0;
  }

  /**
   * Returns the outcome of the game based on the current {@link GameStateInterface}.
   *
   * @param gameContext contains gameScore and gameMap
   * @return a string representing the game outcome
   */
  @Override
  public String getGameOutcome(GameContextRecord gameContext) {
    if (gameContext.gameMap().getEntityCount(entityType) <= 0) {
      return "Level Passed";
    } else {
      return "Game ongoing";
    }
  }

}
