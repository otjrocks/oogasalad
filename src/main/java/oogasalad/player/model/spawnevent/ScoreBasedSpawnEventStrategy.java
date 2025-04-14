package oogasalad.player.model.spawnevent;

import oogasalad.engine.LoggingManager;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.newconfig.model.SpawnEvent;

public class ScoreBasedSpawnEventStrategy implements SpawnEventStrategy {

  @Override
  public boolean shouldSpawn(SpawnEvent spawnEvent, GameContextRecord gameContextRecord,
      double elapsedTime) {
    Object amountObj = spawnEvent.spawnCondition().parameters().get("amount");
    if (amountObj == null) {
      LoggingManager.LOGGER.warn(
          "ScoreBasedSpawnEventStrategy spawnCondition requires amount parameter, but it was not provided in the config, defaulting to never spawning entity.");
      return false;
    }
    try {
      int amount = Integer.parseInt(amountObj.toString());
      return gameContextRecord.gameState().getScore() >= amount;
    } catch (NumberFormatException e) {
      LoggingManager.LOGGER.warn(
          "ScoreBasedSpawnEventStrategy spawnCondition parameter 'amount' must be an integer, but received: {}",
          amountObj);
      return false;
    }
  }

  @Override
  public boolean shouldDespawn(SpawnEvent spawnEvent, GameContextRecord gameContextRecord,
      double elapsedTime) {
    Object amountObj = spawnEvent.despawnCondition().parameters().get("amount");
    if (amountObj == null) {
      LoggingManager.LOGGER.warn(
          "ScoreBasedSpawnEventStrategy despawnCondition requires amount parameter, but it was not provided in the config, defaulting to never despawning entity.");
      return false;
    }
    try {
      int amount = Integer.parseInt(amountObj.toString());
      return gameContextRecord.gameState().getScore() >= amount;
    } catch (NumberFormatException e) {
      LoggingManager.LOGGER.warn(
          "ScoreBasedSpawnEventStrategy despawnCondition parameter 'amount' must be an integer, but received: {}",
          amountObj);
      return false;
    }
  }
}
