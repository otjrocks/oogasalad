package oogasalad.player.model.spawnevent;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.newconfig.model.SpawnEvent;

public class ScoreBasedSpawnEventStrategy implements SpawnEventStrategy {

  @Override
  public boolean shouldSpawn(SpawnEvent spawnEvent, GameContextRecord gameContextRecord,
      double elapsedTime) {
    return gameContextRecord.gameState().getScore() >= (int) spawnEvent.spawnCondition().parameters()
        .get("amount");
  }

  @Override
  public boolean shouldDespawn(SpawnEvent spawnEvent, GameContextRecord gameContextRecord,
      double elapsedTime) {
    return gameContextRecord.gameState().getScore() >= (int) spawnEvent.despawnCondition()
        .parameters().get("amount");
  }
}
