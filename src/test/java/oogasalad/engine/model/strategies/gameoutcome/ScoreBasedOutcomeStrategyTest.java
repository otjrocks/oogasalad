package oogasalad.engine.model.strategies.gameoutcome;

import oogasalad.engine.model.GameState;
import oogasalad.engine.records.GameContextRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// chatgpt this given the tests for entity based outcome strategy and its tests
class ScoreBasedOutcomeStrategyTest {

  private GameState gameState;
  private GameContextRecord context;

  @BeforeEach
  void setUp() {
    gameState = mock(GameState.class);
    context = mock(GameContextRecord.class);
    when(context.gameState()).thenReturn(gameState);
  }

  @Test
  void hasGameEnded_scoreEqualsWinningScore_returnsTrue() {
    when(gameState.getScore()).thenReturn(100);
    ScoreBasedOutcomeStrategy strategy = new ScoreBasedOutcomeStrategy(100);

    assertTrue(strategy.hasGameEnded(context));
  }

  @Test
  void hasGameEnded_scoreGreaterThanWinningScore_returnsTrue() {
    when(gameState.getScore()).thenReturn(150);
    ScoreBasedOutcomeStrategy strategy = new ScoreBasedOutcomeStrategy(100);

    assertTrue(strategy.hasGameEnded(context));
  }

  @Test
  void hasGameEnded_scoreLessThanWinningScore_returnsFalse() {
    when(gameState.getScore()).thenReturn(80);
    ScoreBasedOutcomeStrategy strategy = new ScoreBasedOutcomeStrategy(100);

    assertFalse(strategy.hasGameEnded(context));
  }

  @Test
  void getGameOutcome_whenGameEnded_returnsVictory() {
    when(gameState.getScore()).thenReturn(120);
    ScoreBasedOutcomeStrategy strategy = new ScoreBasedOutcomeStrategy(100);

    String outcome = strategy.getGameOutcome(context);
    assertEquals("Level Passed", outcome);
  }

  @Test
  void getGameOutcome_whenGameOngoing_returnsOngoing() {
    when(gameState.getScore()).thenReturn(50);
    ScoreBasedOutcomeStrategy strategy = new ScoreBasedOutcomeStrategy(100);

    String outcome = strategy.getGameOutcome(context);
    assertEquals("Game ongoing", outcome);
  }
}
