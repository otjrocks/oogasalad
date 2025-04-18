package oogasalad.player.model.strategies.gameoutcome;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.player.model.GameStateInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LivesBasedOutcomeTest {

  private GameStateInterface gameState;
  private GameContextRecord context;
  private LivesBasedOutcomeStrategy strategy;

  @BeforeEach
  void setUp() {
    gameState = mock(GameStateInterface.class);
    context = mock(GameContextRecord.class);
    when(context.gameState()).thenReturn(gameState);
    strategy = new LivesBasedOutcomeStrategy();
  }

  @Test
  void hasGameEnded_livesPositive_returnsFalse() {
    when(gameState.getLives()).thenReturn(3);

    assertFalse(strategy.hasGameEnded(context));
  }

  @Test
  void hasGameEnded_livesNegative_returnsTrue() {
    when(gameState.getLives()).thenReturn(-1);

    assertTrue(strategy.hasGameEnded(context));
  }

  @Test
  void hasGameEnded_livesZero_returnsTrue() {
    when(gameState.getLives()).thenReturn(0);

    assertTrue(strategy.hasGameEnded(context));
  }

  @Test
  void hasGameEnded_livesPositive_returnsOngoing() {
    when(gameState.getLives()).thenReturn(3);

    assertEquals("Game ongoing", strategy.getGameOutcome(context));
  }

  @Test
  void hasGameEnded_livesNegative_returnsGameOver() {
    when(gameState.getLives()).thenReturn(-1);

    assertEquals("Game Over", strategy.getGameOutcome(context));
  }

  @Test
  void hasGameEnded_livesZero_returnsGameOver() {
    when(gameState.getLives()).thenReturn(0);

    assertEquals("Game Over", strategy.getGameOutcome(context));
  }

}
