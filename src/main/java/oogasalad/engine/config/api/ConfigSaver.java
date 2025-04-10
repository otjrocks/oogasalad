package oogasalad.engine.config.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.file.Path;
import oogasalad.engine.config.ConfigException;

/**
 * Interface for saving various parts of a game configuration into separate JSON files.
 * <p>
 * This interface supports saving the top-level game metadata, individual level configurations,
 * and entity type definitions using Jackson {@link ObjectNode}s. Implementing classes are responsible
 * for writing these JSON objects to a specified folder in the desired format (e.g., pretty-printed JSON).
 * </p>
 *
 * <p>Typical usage involves calling {@code saveGameConfig} to write the root configuration file,
 * {@code saveLevel} for each level layout, and {@code saveEntityType} for each entity's behavior and visual data.</p>
 *
 * @author Will He
 */
public interface ConfigSaver {

  /**
   * Saves the overall game metadata and high-level structure to a JSON file in the specified folder.
   * <p>
   * This includes game title, author, description, default settings, and level references.
   * </p>
   *
   * @param gameConfigJson the JSON object representing the game configuration
   * @param gameFolder     the folder to save the game config into (e.g., "output/")
   */
  void saveGameConfig(ObjectNode gameConfigJson, Path gameFolder) throws ConfigException;

  /**
   * Saves the configuration for a single game level to a separate JSON file in the specified folder.
   * <p>
   * The level file typically contains the grid layout, entity ID mappings, and settings like grid size or edge policy.
   * </p>
   *
   * @param levelName  the name of the level file to create (without ".json" extension)
   * @param levelJson  the JSON object representing the level configuration
   * @param gameFolder the folder to save the level file into
   */
  void saveLevel(String levelName, ObjectNode levelJson, Path gameFolder) throws ConfigException;

  /**
   * Saves a specific entity type's configuration to its own JSON file in the given folder.
   * <p>
   * This includes control strategy, modes, movement speed, image data, and collision rules.
   * </p>
   *
   * @param entityName the name of the entity (used as the filename)
   * @param entityJson the JSON object representing the entity's configuration
   * @param gameFolder the folder to save the entity file into
   */
  void saveEntityType(String entityName, ObjectNode entityJson, Path gameFolder)
      throws ConfigException;
}
