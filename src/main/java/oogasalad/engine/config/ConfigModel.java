package oogasalad.engine.config;

import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameSettings;
import oogasalad.engine.model.MetaData;
import oogasalad.engine.model.Tiles;
import java.util.List;
import oogasalad.engine.records.config.model.ParsedLevel;
import oogasalad.engine.records.config.model.wincondition.WinCondition;

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
public record ConfigModel(MetaData metadata,
                          GameSettings settings,
                          List<EntityType> entityTypes,
                          List<ParsedLevel> levels,
                          List<CollisionRule> collisionRules,
                          WinCondition winCondition,
                          List<Tiles> tiles) {

}
