package oogasalad.player.model.spawnevent;

/**
 * A factory design pattern that creates a spawn event strategy based on the provided type string.
 *
 * @author Owen Jennings
 */
public class SpawnEventStrategyFactory {

  private static final String STRATEGY_PACKAGE = "oogasalad.player.model.spawnevent";

  /**
   * Create the correct SpawnEventStrategy corresponding to the type provided.
   *
   * @param type The type string of the spawn event strategy you wish to create.
   * @return A spawn event strategy of the correct type.
   */
  public static SpawnEventStrategy createSpawnEventStrategy(String type) {
    try {
      Class<?> clazz = Class.forName(STRATEGY_PACKAGE + "." + type + "SpawnEventStrategy");
      if (SpawnEventStrategy.class.isAssignableFrom(clazz)) {
        return (SpawnEventStrategy) clazz.getDeclaredConstructor().newInstance();
      } else {
        throw new IllegalArgumentException(
            type + "SpawnEventStrategy does not implement SpawnEventStrategy.");
      }
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Strategy class not found for type: " + type, e);
    } catch (Exception e) {
      throw new RuntimeException("Failed to instantiate strategy for type: " + type, e);
    }
  }
}
