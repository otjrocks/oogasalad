package oogasalad.engine.model;

import java.util.List;
import java.util.Map;
import oogasalad.engine.config.ModeConfig;

/**
 * Represents a reusable template for creating game entities.
 * This class defines the static properties of an entity type,
 * including its control behavior, visual modes, blocking behavior,
 * effects, and movement strategy configuration.
 * <p>
 * Used for entity instantiation and behavior setup in the game engine.
 * </p>
 *
 * Example types: "Pacman", "Ghost", "Wall", etc.
 * Example control types: "Keyboard", "BFS", "TargetEntity"
 *
 * @author Will He
 */
public class EntityType {

  /** The string identifier for this entity type (e.g., "Pacman", "Wall"). */
  private String type;

  /** The control mechanism for the entity (e.g., "Keyboard", "TargetEntity", "BFS"). */
  private String controlType;

  /** Optional effect applied when this entity is active (e.g., "SpeedBoost(5)"). */
  private String effect;

  /**
   * A mapping from mode name (e.g., "Default", "PoweredUp") to configuration
   * settings (image, speed, etc.) for that mode.
   */
  private Map<String, ModeConfig> modes;

  /**
   * A list of entity type names that this entity blocks during movement
   * or collision detection (e.g., ["Pacman", "Ghost"]).
   */
  private List<String> blocks;

  /**
   * Optional configuration for AI strategies or movement logic.
   * Keys and values are strategy-specific.
   */
  private Map<String, Object> strategyConfig;

  /** Default constructor required for deserialization or reflection. */
  public EntityType() {
    // Empty
  }

  /**
   * Returns the type identifier of the entity.
   * @return entity type string
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type identifier for the entity.
   * @param type the name of the entity type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the control type for the entity.
   * @return the control mechanism string
   */
  public String getControlType() {
    return controlType;
  }

  /**
   * Sets the control type for the entity.
   * @param controlType the control strategy name (e.g., "Keyboard", "BFS")
   */
  public void setControlType(String controlType) {
    this.controlType = controlType;
  }

  /**
   * Returns the effect string applied to the entity.
   * @return the effect description (e.g., "SpeedBoost(5)")
   */
  public String getEffect() {
    return effect;
  }

  /**
   * Sets the effect string applied to the entity.
   * @param effect the effect description string
   */
  public void setEffect(String effect) {
    this.effect = effect;
  }

  /**
   * Returns the mode configuration map for the entity.
   * @return a map from mode names to ModeConfig instances
   */
  public Map<String, ModeConfig> getModes() {
    return modes;
  }

  /**
   * Sets the mode configuration map for the entity.
   * @param modes the mode-to-configuration mapping
   */
  public void setModes(Map<String, ModeConfig> modes) {
    this.modes = modes;
  }

  /**
   * Returns a list of entity types that this entity blocks.
   * @return a list of type names this entity prevents movement through
   */
  public List<String> getBlocks() {
    return blocks;
  }

  /**
   * Sets the list of entity types that this entity blocks.
   * @param blocks the list of type names this entity blocks
   */
  public void setBlocks(List<String> blocks) {
    this.blocks = blocks;
  }

  /**
   * Returns the optional movement strategy configuration map.
   * @return a map of strategy-specific configuration key-value pairs
   */
  public Map<String, Object> getStrategyConfig() {
    return strategyConfig;
  }

  /**
   * Sets the movement strategy configuration for the entity.
   * @param strategyConfig the configuration map for AI or custom movement
   */
  public void setStrategyConfig(Map<String, Object> strategyConfig) {
    this.strategyConfig = strategyConfig;
  }
}
