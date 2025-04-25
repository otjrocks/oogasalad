package oogasalad.player.model.api;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import oogasalad.player.model.strategies.spawnevent.ScoreBasedSpawnEventStrategy;
import oogasalad.player.model.strategies.spawnevent.SpawnEventStrategyInterface;
import oogasalad.player.model.strategies.spawnevent.TimeElapsedSpawnEventStrategy;
import org.junit.jupiter.api.Test;

class SpawnEventStrategyFactoryTest {
  // ChatGPT assisted in generating these tests.
  @Test
  void createSpawnEventStrategy_scoreBased_returnsScoreBasedSpawnEventStrategy() {
    SpawnEventStrategyInterface strategy = SpawnEventStrategyFactory.createSpawnEventStrategy("ScoreBased");
    assertNotNull(strategy);
    assertInstanceOf(ScoreBasedSpawnEventStrategy.class, strategy);
  }

  @Test
  void createSpawnEventStrategy_timeElapsed_returnsTimeElapsedSpawnEventStrategy() {
    SpawnEventStrategyInterface strategy = SpawnEventStrategyFactory.createSpawnEventStrategy("TimeElapsed");
    assertNotNull(strategy);
    assertInstanceOf(TimeElapsedSpawnEventStrategy.class, strategy);
  }

  @Test
  void createSpawnEventStrategy_invalidType_throwsIllegalArgumentException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        SpawnEventStrategyFactory.createSpawnEventStrategy("NonExistent"));
    assertTrue(exception.getMessage().contains("Strategy class not found"));
  }
}
