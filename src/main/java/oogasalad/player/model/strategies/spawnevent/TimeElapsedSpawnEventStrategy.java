package oogasalad.player.model.strategies.spawnevent;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.model.SpawnEventRecord;
import oogasalad.engine.utility.LoggingManager;

/**
 * A spawn event strategy that handles TimeElapsed conditions.
 *
 * @author Owen Jennings
 */
public class TimeElapsedSpawnEventStrategy implements SpawnEventStrategyInterface {
  // I used ChatGPT to refactor parts of this code.

  @Override
  public boolean shouldSpawn(SpawnEventRecord spawnEvent, GameContextRecord gameContextRecord) {
    Object amountObj = spawnEvent.spawnCondition().parameters().get("amount");
    if (amountObj == null) {
      LoggingManager.LOGGER.warn(
          "TimeElapsedSpawnEventStrategy spawnCondition requires amount parameter, but it was not provided in the config, defaulting to never spawning entity.");
      return false;
    }
    try {
      int amount = Integer.parseInt(amountObj.toString());
      return gameContextRecord.gameState().getTimeElapsed() >= amount;
    } catch (NumberFormatException e) {
      LoggingManager.LOGGER.warn(
          "TimeElapsedSpawnEventStrategy spawnCondition parameter 'amount' must be an integer, but received: {}",
          amountObj);
      return false;
    }
  }

  @Override
  public boolean shouldDespawn(SpawnEventRecord spawnEvent, GameContextRecord gameContextRecord) {
    Object amountObj = spawnEvent.despawnCondition().parameters().get("amount");
    if (amountObj == null) {
      LoggingManager.LOGGER.warn(
          "TimeElapsedSpawnEventStrategy despawnCondition requires amount parameter, but it was not provided in the config, defaulting to never despawning entity.");
      return false;
    }
    try {
      int amount = Integer.parseInt(amountObj.toString());
      return gameContextRecord.gameState().getTimeElapsed() >= amount;
    } catch (NumberFormatException e) {
      LoggingManager.LOGGER.warn(
          "TimeElapsedSpawnEventStrategy despawnCondition parameter 'amount' must be an integer, but received: {}",
          amountObj);
      return false;
    }
  }
}
