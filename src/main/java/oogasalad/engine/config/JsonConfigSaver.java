package oogasalad.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.file.Path;
import oogasalad.engine.config.api.ConfigSaver;

/**
 * Implementation of {@link ConfigSaver} that writes configuration data as JSON files
 * using the Jackson library.
 * <p>
 * This class supports saving:
 * <ul>
 *   <li>Top-level game configuration (game metadata and level list)</li>
 *   <li>Individual level layouts</li>
 *   <li>Entity type definitions (modes, images, logic)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Files are saved in a specified directory, and formatted with indentation
 * for readability.
 * </p>
 *
 * Example usage:
 * <pre>{@code
 * JsonConfigSaver saver = new JsonConfigSaver();
 * ObjectNode gameJson = ...;
 * Path folder = Path.of("output/");
 * saver.saveGameConfig(gameJson, folder);
 * }</pre>
 *
 * @author Will He
 */
public class JsonConfigSaver implements ConfigSaver {

  private final ObjectMapper mapper;

  /**
   * Constructs a new JsonConfigSaver using Jackson's {@link ObjectMapper}.
   * Pretty-printing is enabled by default.
   */
  public JsonConfigSaver() {
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  /**
   * Saves the top-level game configuration JSON as {@code gameConfig.json} in the given folder.
   *
   * @param config the JSON representation of the game config
   * @param folder the folder to save to
   */
  @Override
  public void saveGameConfig(ObjectNode config, Path folder) {
    writeJson(config, folder.resolve("gameConfig.json"));
  }

  /**
   * Saves a level configuration as a separate file in the given folder.
   * The filename is based on the level's name.
   *
   * @param name   the base filename (without .json)
   * @param config the level's JSON representation
   * @param folder the folder to save to
   */
  @Override
  public void saveLevel(String name, ObjectNode config, Path folder) {
    writeJson(config, folder.resolve(name + ".json"));
  }

  /**
   * Saves an entity type configuration JSON in the given folder.
   * The filename will be the entity's name in lowercase.
   *
   * @param name   the entity type name
   * @param config the entity config as JSON
   * @param folder the folder to save to
   */
  @Override
  public void saveEntityType(String name, ObjectNode config, Path folder) {
    writeJson(config, folder.resolve(name.toLowerCase() + ".json"));
  }

  /**
   * Writes the given JSON object to the specified file path.
   *
   * @param config the JSON object to write
   * @param path   the file path to write to
   * @throws RuntimeException if the file cannot be written
   */
  private void writeJson(ObjectNode config, Path path) {
    try {
      mapper.writeValue(path.toFile(), config);
    } catch (IOException e) {
      throw new RuntimeException("Failed to write: " + path, e);
    }
  }
}
