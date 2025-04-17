package oogasalad.engine.model.strategies.gameoutcome;

import oogasalad.engine.model.GameMapInterface;
import oogasalad.engine.records.GameContextRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EntityBasedOutcomeStrategyTest {

  private GameMapInterface gameMap;
  private GameContextRecord context;

  @BeforeEach
  void setUp() {
    gameMap = mock(GameMapInterface.class);
    context = mock(GameContextRecord.class);
    when(context.gameMap()).thenReturn(gameMap);
  }

  @Test
  void hasGameEnded_entityCountZero_returnsTrue() {
    when(gameMap.getEntityCount("Pellet")).thenReturn(0);

    EntityBasedOutcomeStrategy strategy = new EntityBasedOutcomeStrategy("Pellet");

    assertTrue(strategy.hasGameEnded(context));
    verify(gameMap).getEntityCount("Pellet");
  }

  @Test
  void hasGameEnded_entityCountNegative_returnsTrue() {
    when(gameMap.getEntityCount("Pellet")).thenReturn(-1);

    EntityBasedOutcomeStrategy strategy = new EntityBasedOutcomeStrategy("Pellet");

    assertTrue(strategy.hasGameEnded(context));
    verify(gameMap).getEntityCount("Pellet");
  }

  @Test
  void hasGameEnded_entityCountPositive_returnsFalse() {
    when(gameMap.getEntityCount("Pellet")).thenReturn(5);

    EntityBasedOutcomeStrategy strategy = new EntityBasedOutcomeStrategy("Pellet");

    assertFalse(strategy.hasGameEnded(context));
    verify(gameMap).getEntityCount("Pellet");
  }

  @Test
  void getGameOutcome_whenGameEnded_returnsVictory() {
    when(gameMap.getEntityCount("Pellet")).thenReturn(0);

    EntityBasedOutcomeStrategy strategy = new EntityBasedOutcomeStrategy("Pellet");

    String outcome = strategy.getGameOutcome(context);

    assertEquals("Level Passed", outcome);
    verify(gameMap).getEntityCount("Pellet");
  }

  @Test
  void getGameOutcome_whenGameOngoing_returnsOngoing() {
    when(gameMap.getEntityCount("Pellet")).thenReturn(2);

    EntityBasedOutcomeStrategy strategy = new EntityBasedOutcomeStrategy("Pellet");

    String outcome = strategy.getGameOutcome(context);

    assertEquals("Game ongoing", outcome);
    verify(gameMap).getEntityCount("Pellet");
  }
}
