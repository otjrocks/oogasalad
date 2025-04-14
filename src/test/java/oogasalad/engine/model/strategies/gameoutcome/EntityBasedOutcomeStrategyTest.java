package oogasalad.engine.model.strategies.gameoutcome;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.records.GameContextRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EntityBasedOutcomeStrategyTest {

  private GameMap gameMap;
  private GameContextRecord context;

  @BeforeEach
  void setUp() {
    gameMap = mock(GameMap.class);
    context = mock(GameContextRecord.class);
    when(context.gameMap()).thenReturn(gameMap);
  }

  @Test
  void hasGameEnded_entityCountZero_returnsTrue() {
    when(gameMap.getEntityCount("Pellet")).thenReturn(0);

    EntityBasedOutcomeStrategy strategy = new EntityBasedOutcomeStrategy("Pellet");

    assertTrue(strategy.hasGameEnded(context));
    verify(gameMap, never()).decrementEntityCount("Pellet");
  }

  @Test
  void hasGameEnded_entityCountNegative_returnsTrue() {
    when(gameMap.getEntityCount("Pellet")).thenReturn(-1);

    EntityBasedOutcomeStrategy strategy = new EntityBasedOutcomeStrategy("Pellet");

    assertTrue(strategy.hasGameEnded(context));
    verify(gameMap, never()).decrementEntityCount("Pellet");
  }

  @Test
  void hasGameEnded_entityCountPositive_returnsFalseAndDecrements() {
    when(gameMap.getEntityCount("Pellet")).thenReturn(5);

    EntityBasedOutcomeStrategy strategy = new EntityBasedOutcomeStrategy("Pellet");

    assertFalse(strategy.hasGameEnded(context));
    verify(gameMap).decrementEntityCount("Pellet");
  }

  @Test
  void getGameOutcome_whenGameEnded_returnsVictory() {
    when(gameMap.getEntityCount("Pellet")).thenReturn(0);

    EntityBasedOutcomeStrategy strategy = new EntityBasedOutcomeStrategy("Pellet");

    String outcome = strategy.getGameOutcome(context);

    assertEquals("Level Passed", outcome);
  }

  @Test
  void getGameOutcome_whenGameOngoing_returnsOngoing() {
    when(gameMap.getEntityCount("Pellet")).thenReturn(2);

    EntityBasedOutcomeStrategy strategy = new EntityBasedOutcomeStrategy("Pellet");

    String outcome = strategy.getGameOutcome(context);

    assertEquals("Game ongoing", outcome);
    verify(gameMap).decrementEntityCount("Pellet");
  }
}
