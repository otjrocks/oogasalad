package oogasalad.engine.model.strategies.gameoutcome;

import oogasalad.engine.model.GameState;
import oogasalad.engine.records.GameContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivesBasedOutcomeTest {

  private GameState gameState;
  private GameContext context;
  private LivesBasedOutcome strategy;

  @BeforeEach
  void setUp() {
    gameState = mock(GameState.class);
    context = mock(GameContext.class);
    when(context.gameState()).thenReturn(gameState);
    strategy = new LivesBasedOutcome();
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

    assertEquals("Game Over! You ran out of lives!", strategy.getGameOutcome(context));
  }

  @Test
  void hasGameEnded_livesZero_returnsGameOver() {
    when(gameState.getLives()).thenReturn(0);

    assertEquals("Game Over! You ran out of lives!", strategy.getGameOutcome(context));
  }

}
