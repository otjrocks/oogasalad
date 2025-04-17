package oogasalad.player.model.strategies.gameoutcome;

import oogasalad.player.model.GameStateInterface;
import oogasalad.engine.records.GameContextRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
