package oogasalad.engine.config;

import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameSettings;
import oogasalad.engine.model.MetaData;
import oogasalad.engine.model.Tiles;
import java.util.List;

/**
 * The ConfigModel class serves as a container for the configuration data of a game. It encapsulates
 * metadata, game settings, entity configurations, collision rules, and the win condition. This
 * class provides getter and setter methods to access and modify these properties.
 *
 * <p>Key components of the configuration include:
 * <ul>
 *   <li>MetaData: General information about the game.</li>
 *   <li>GameSettings: Settings that define the game's behavior and rules.</li>
 *   <li>EntityType: A list of configurations for game entities.</li>
 *   <li>CollisionRule: A list of rules governing entity interactions.</li>
 *   <li>WinCondition: A string defining the condition for winning the game.</li>
 * </ul>
 *
 * @author Will He
 */
public class ConfigModel {

  private MetaData metadata;
  private GameSettings settings;
  private List<EntityType> entityTypes;
  private List<EntityPlacement> entityPlacements;
  private List<CollisionRule> collisionRules;
  private String winCondition;
  private List<Tiles> tiles;

  /**
   * Retrieves the metadata of the game.
   *
   * @return the metadata of the game
   */
  public MetaData getMetadata() {
    return metadata;
  }

  /**
   * Sets the metadata of the game.
   *
   * @param metadata the metadata to set
   */
  public void setMetadata(MetaData metadata) {
    this.metadata = metadata;
  }

  /**
   * Retrieves the game settings.
   *
   * @return the game settings
   */
  public GameSettings getSettings() {
    return settings;
  }

  /**
   * Sets the game settings.
   *
   * @param settings the game settings to set
   */
  public void setSettings(GameSettings settings) {
    this.settings = settings;
  }

  /**
   * Retrieves the list of entity placements.
   *
   * @return the list of entity placements
   */
  public List<EntityPlacement> getEntityPlacements() {
    return entityPlacements;
  }

  /**
   * Sets the list of entity placements.
   *
   * @param entityConfigs the list of entity placements to set
   */
  public void setEntityPlacements(List<EntityPlacement> entityConfigs) {
    this.entityPlacements = entityConfigs;
  }

  /**
   * Retrieves the list of entity types.
   *
   * @return the list of entity types
   */
  public List<EntityType> getEntityTypes() {
    return entityTypes;
  }

  /**
   * Sets the list of entity types.
   *
   * @param entityTypes the list of entity types to set
   */
  public void setEntityTypes(List<EntityType> entityTypes) {
    this.entityTypes = entityTypes;
  }

  /**
   * Retrieves the list of collision rules.
   *
   * @return the list of collision rules
   */
  public List<CollisionRule> getCollisionRules() {
    return collisionRules;
  }

  /**
   * Sets the list of collision rules.
   *
   * @param collisionRules the list of collision rules to set
   */
  public void setCollisionRules(List<CollisionRule> collisionRules) {
    this.collisionRules = collisionRules;
  }

  /**
   * Retrieves the win condition of the game.
   *
   * @return the win condition of the game
   */
  public String getWinCondition() {
    return winCondition;
  }

  /**
   * Sets the win condition of the game.
   *
   * @param winCondition the win condition to set
   */
  public void setWinCondition(String winCondition) {
    this.winCondition = winCondition;
  }
  /**
   * Retrieves the list of tiles for the game.
   *
   * @return the list of tiles for the game
   */
  public List<Tiles> getTiles() {
    return tiles;
  }
  /**
   * Sets the tiles of the game.
   *
   * @param tiles the tiles in the game
   */
  public void setTiles(List<Tiles> tiles) {
    this.tiles = tiles;
  }

}
