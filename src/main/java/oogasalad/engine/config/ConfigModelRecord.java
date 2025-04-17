package oogasalad.engine.config;

import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.model.EntityTypeRecord;
import oogasalad.engine.model.GameSettingsRecord;
import oogasalad.engine.model.MetaDataRecord;
import java.util.List;
import oogasalad.engine.records.config.model.ParsedLevelRecord;
import oogasalad.engine.records.config.model.losecondition.LoseConditionInterface;
import oogasalad.engine.records.config.model.wincondition.WinConditionInterface;

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
public record ConfigModelRecord(MetaDataRecord metadata,
                                GameSettingsRecord settings,
                                List<EntityTypeRecord> entityTypes,
                                List<ParsedLevelRecord> levels,
                                List<CollisionRule> collisionRules,
                                WinConditionInterface winCondition,
                                LoseConditionInterface loseCondition,
                                int currentLevelIndex) {

}
