package oogasalad.player.model.spawnevent;

import oogasalad.engine.model.Condition;
import oogasalad.engine.model.GameMapImpl;
import oogasalad.engine.model.GameState;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.model.SpawnEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimeElapsedSpawnEventStrategyTest {

  private TimeElapsedSpawnEventStrategy strategy;

  private GameContextRecord contextWithTime(double timeElapsed) {
    GameState mockState = mock(GameState.class);
    when(mockState.getTimeElapsed()).thenReturn(timeElapsed);
    return new GameContextRecord(new GameMapImpl(10, 10), mockState);
  }

  private SpawnEvent spawnEventWithParams(String amountValue, boolean forSpawn) {
    Condition spawnCondition = new Condition("TimeElapsed", Map.of("amount", amountValue));
    Condition despawnCondition = new Condition("TimeElapsed", Map.of("amount", amountValue));
    return new SpawnEvent(
        null,
        forSpawn ? spawnCondition : new Condition("TimeElapsed", Map.of()),
        0, 0, "test",
        forSpawn ? new Condition("TimeElapsed", Map.of()) : despawnCondition
    );
  }

  @BeforeEach
  void setup() {
    strategy = new TimeElapsedSpawnEventStrategy();
  }

  @Test
  void shouldSpawn_timeAboveThreshold_returnsTrue() {
    SpawnEvent spawnEvent = spawnEventWithParams("5", true);
    GameContextRecord context = contextWithTime(10.0);
    assertTrue(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldSpawn_timeBelowThreshold_returnsFalse() {
    SpawnEvent spawnEvent = spawnEventWithParams("10", true);
    GameContextRecord context = contextWithTime(5.0);
    assertFalse(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldSpawn_missingAmountParam_returnsFalse() {
    SpawnEvent spawnEvent = new SpawnEvent(
        null,
        new Condition("TimeElapsed", Map.of()),
        0, 0, "test",
        new Condition("TimeElapsed", Map.of())
    );
    GameContextRecord context = contextWithTime(10.0);
    assertFalse(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldSpawn_invalidAmountParam_returnsFalse() {
    SpawnEvent spawnEvent = spawnEventWithParams("invalid", true);
    GameContextRecord context = contextWithTime(10.0);
    assertFalse(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_timeAboveThreshold_returnsTrue() {
    SpawnEvent spawnEvent = spawnEventWithParams("10", false);
    GameContextRecord context = contextWithTime(15.0);
    assertTrue(strategy.shouldDespawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_timeBelowThreshold_returnsFalse() {
    SpawnEvent spawnEvent = spawnEventWithParams("10", false);
    GameContextRecord context = contextWithTime(5.0);
    assertFalse(strategy.shouldDespawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_missingAmountParam_returnsFalse() {
    SpawnEvent spawnEvent = new SpawnEvent(
        null,
        new Condition("TimeElapsed", Map.of()),
        0, 0, "test",
        new Condition("TimeElapsed", Map.of())
    );
    GameContextRecord context = contextWithTime(10.0);
    assertFalse(strategy.shouldDespawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_invalidAmountParam_returnsFalse() {
    SpawnEvent spawnEvent = spawnEventWithParams("notAnInt", false);
    GameContextRecord context = contextWithTime(10.0);
    assertFalse(strategy.shouldDespawn(spawnEvent, context));
  }
}
