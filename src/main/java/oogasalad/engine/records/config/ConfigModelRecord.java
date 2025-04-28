package oogasalad.engine.records.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import oogasalad.engine.config.CollisionRule;
import oogasalad.engine.records.config.model.ParsedLevelRecord;
import oogasalad.engine.records.config.model.SettingsRecord;
import oogasalad.engine.records.config.model.losecondition.LoseConditionInterface;
import oogasalad.engine.records.config.model.wincondition.WinConditionInterface;
import oogasalad.engine.records.model.EntityTypeRecord;
import oogasalad.engine.records.model.MetaDataRecord;

/**
 * The ConfigModel record serves as a container for the configuration data of a game. It
 * encapsulates metadata, game settings, entity configurations, collision rules, and the win
 * condition. This record provides getter and setter methods to access and modify these properties.
 * This model does not contain any setter methods (uses record) to enforce explicit immutability.
 *
 * <p>Key components of the configuration include:
 * <ul>
 *   <li>MetaData: General information about the game.</li>
 *   <li>GameSettings: Settings that define the game's behavior and rules.</li>
 *   <li>EntityType: A list of configurations for game entities.</li>
 *   <li>CollisionRule: A list of rules governing entity interactions.</li>
 *   <li>WinCondition: A WinCondition record defining the condition for winning the game.</li>
 * </ul>
 *
 * @author Will He, Owen Jennings
 */
public record ConfigModelRecord(
    MetaDataRecord metadata,
    SettingsRecord settings,
    List<EntityTypeRecord> entityTypes,
    List<ParsedLevelRecord> levels,
    List<CollisionRule> collisionRules,
    WinConditionInterface winCondition,
    LoseConditionInterface loseCondition,
    int currentLevelIndex,
    Map<String, Double> respawnableEntities
) {

  /**
   * Returns the corresponding entity type to its entity name
   *
   * @param name represents the entity type name stored in the json file
   */
  public EntityTypeRecord getEntityTypeByName(String name) {
    return entityTypes().stream()
        .filter(e -> e.type().equals(name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Entity type not found: " + name));
  }

}

