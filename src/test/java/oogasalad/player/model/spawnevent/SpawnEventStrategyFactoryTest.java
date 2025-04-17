package oogasalad.player.model.spawnevent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpawnEventStrategyFactoryTest {
  // ChatGPT assisted in generating these tests.
  @Test
  void createSpawnEventStrategy_scoreBased_returnsScoreBasedSpawnEventStrategy() {
    SpawnEventStrategyInterface strategy = SpawnEventStrategyFactory.createSpawnEventStrategy("ScoreBased");
    assertNotNull(strategy);
    assertTrue(strategy instanceof ScoreBasedSpawnEventStrategy);
  }

  @Test
  void createSpawnEventStrategy_timeElapsed_returnsTimeElapsedSpawnEventStrategy() {
    SpawnEventStrategyInterface strategy = SpawnEventStrategyFactory.createSpawnEventStrategy("TimeElapsed");
    assertNotNull(strategy);
    assertTrue(strategy instanceof TimeElapsedSpawnEventStrategy);
  }

  @Test
  void createSpawnEventStrategy_invalidType_throwsIllegalArgumentException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        SpawnEventStrategyFactory.createSpawnEventStrategy("NonExistent"));
    assertTrue(exception.getMessage().contains("Strategy class not found"));
  }
}
