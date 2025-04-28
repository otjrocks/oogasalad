package oogasalad.player.model.strategies.gameoutcome;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.player.model.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeBasedOutcomeStrategyTest {
  // Test wrote by ChatGPT
  private GameState mockGameState;
  private GameContextRecord mockGameContext;
  private TimeBasedOutcomeStrategy strategy;

  @BeforeEach
  void setUp() {
    mockGameState = mock(GameState.class);
    mockGameContext = mock(GameContextRecord.class);
    when(mockGameContext.gameState()).thenReturn(mockGameState);
  }

  @Test
  void testGameNotEndedWhenTimeIsLessThanLimit() {
    when(mockGameState.getTimeElapsed()).thenReturn(5.0);
    strategy = new TimeBasedOutcomeStrategy(10);

    assertFalse(strategy.hasGameEnded(mockGameContext));
    assertEquals("Game ongoing", strategy.getGameOutcome(mockGameContext));
  }

  @Test
  void testGameEndedWhenTimeEqualsLimit() {
    when(mockGameState.getTimeElapsed()).thenReturn(10.0);
    strategy = new TimeBasedOutcomeStrategy(10);

    assertTrue(strategy.hasGameEnded(mockGameContext));
    assertEquals("Game Over", strategy.getGameOutcome(mockGameContext));
  }

  @Test
  void testGameEndedWhenTimeExceedsLimit() {
    when(mockGameState.getTimeElapsed()).thenReturn(15.0);
    strategy = new TimeBasedOutcomeStrategy(10);

    assertTrue(strategy.hasGameEnded(mockGameContext));
    assertEquals("Game Over", strategy.getGameOutcome(mockGameContext));
  }

  @Test
  void testDifferentTimeLimits() {
    when(mockGameState.getTimeElapsed()).thenReturn(30.0);

    TimeBasedOutcomeStrategy shortGame = new TimeBasedOutcomeStrategy(10);
    TimeBasedOutcomeStrategy longGame = new TimeBasedOutcomeStrategy(50);

    assertTrue(shortGame.hasGameEnded(mockGameContext));
    assertEquals("Game Over", shortGame.getGameOutcome(mockGameContext));

    assertFalse(longGame.hasGameEnded(mockGameContext));
    assertEquals("Game ongoing", longGame.getGameOutcome(mockGameContext));
  }
}
