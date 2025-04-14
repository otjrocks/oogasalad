package oogasalad.player.model.spawnevent;

public class SpawnEventStrategyFactory {

  public static SpawnEventStrategy createSpawnEventStrategy(String type) {
    if (type.equals("TimeElapsed")) {
      return new TimeElapsedSpawnEventStrategy();
    }
    if (type.equals("ScoreBased")) {
      return new ScoreBasedSpawnEventStrategy();
    }
    return null;
  }
}
