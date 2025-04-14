package oogasalad.player.model.spawnevent;

import oogasalad.engine.LoggingManager;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.newconfig.model.SpawnEvent;

public class TimeElapsedSpawnEventStrategy implements SpawnEventStrategy {

  @Override
  public boolean shouldSpawn(SpawnEvent spawnEvent, GameContextRecord gameContextRecord,
      double elapsedTime) {
    if (!spawnEvent.spawnCondition().parameters().containsKey("amount")) {
      LoggingManager.LOGGER.warn(
          "TimeElapsedSpawnEventStrategy spawnCondition requires amount parameter, but it was not provided in the config, defaulting to never spawning entity.");
      return false;
    }
    return elapsedTime >= (int) spawnEvent.spawnCondition().parameters().get("amount");
  }

  @Override
  public boolean shouldDespawn(SpawnEvent spawnEvent, GameContextRecord gameContextRecord,
      double elapsedTime) {
    if (!spawnEvent.despawnCondition().parameters().containsKey("amount")) {
      LoggingManager.LOGGER.warn(
          "TimeElapsedSpawnEventStrategy despawn condition requires amount parameter, but it was not provided in the config, defaulting to never despawning entity.");
      return false;
    }
    return elapsedTime >= (int) spawnEvent.despawnCondition().parameters().get("amount");
  }
}
