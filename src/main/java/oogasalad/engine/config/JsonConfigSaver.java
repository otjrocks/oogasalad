package oogasalad.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import oogasalad.engine.config.api.ConfigSaverInterface;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.utility.LoggingManager;

/**
 * Implementation of {@link ConfigSaverInterface} that writes configuration data as JSON files using
 * the Jackson library.
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
 * <p>
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
public class JsonConfigSaver implements ConfigSaverInterface {

  private final ObjectMapper mapper;
  private static final String CORE_FOLDER = "core";


  /**
   * Constructs a new JsonConfigSaver using Jackson's {@link ObjectMapper}. Pretty-printing is
   * enabled by default.
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
  public void saveGameConfig(ObjectNode config, Path folder) throws ConfigException {
    writeJson(config, folder.resolve("gameConfig.json"));
  }

  /**
   * Saves a level configuration as a separate file in the given folder. The filename is based on
   * the level's name.
   *
   * @param name   the base filename (without .json)
   * @param config the level's JSON representation
   * @param folder the folder to save to
   */
  @Override
  public void saveLevel(String name, ObjectNode config, Path folder) throws ConfigException {
    writeJson(config, folder.resolve(CORE_FOLDER + "/maps/" + name + ".json"));
  }

  /**
   * Saves an entity type configuration JSON in the given folder. The filename will be the entity's
   * name in lowercase.
   *
   * @param name   the entity type name
   * @param config the entity config as JSON
   * @param folder the folder to save to
   */
  @Override
  public void saveEntityType(String name, ObjectNode config, Path folder) throws ConfigException {
    writeJson(config, folder.resolve(CORE_FOLDER + "/entities/" + name + ".json"));
  }

  /**
   * Saves the current level into the configuration file.
   *
   * @param newLevelIndex Level to save as curr level
   * @param folder        the folder to save to
   */
  public void saveUpdatedLevelIndex(int newLevelIndex, Path folder)
      throws ConfigException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    Path configPath = folder.resolve("gameConfig.json");
    try {
      ObjectNode root = (ObjectNode) mapper.readTree(configPath.toFile());
      root.put("currentLevelIndex", newLevelIndex);
      mapper.writeValue(configPath.toFile(), root);
    } catch (IOException e) {
      throw new ConfigException("Failed to update currentLevelIndex in gameConfig.json", e);
    }
  }


  /**
   * Writes the given JSON object to the specified file path.
   *
   * @param config the JSON object to write
   * @param path   the file path to write to
   * @throws RuntimeException if the file cannot be written
   */
  private void writeJson(ObjectNode config, Path path) throws ConfigException {
    try {
      // Ensure parent directories exist
      if (path.getParent() != null) {
        Files.createDirectories(path.getParent());
      }
      mapper.writeValue(path.toFile(), config);
    } catch (IOException e) {
      throw new ConfigException("Failed to write: " + path, e);
    }
  }

  /**
   * Copy a game asset (i.e. image) from a current location into the game asset's folder.
   *
   * @param currentAssetPath The current path of the asset on the user's device.
   * @param folder           The folder to copy the asset to.
   * @throws ConfigException An error if the asset cannot be copied.
   */
  public void writeAsset(String currentAssetPath, Path folder) throws ConfigException {
    Path assetsFolder = folder.resolve("assets");
    try {
      Files.createDirectories(assetsFolder);

      Path source;
      if (currentAssetPath.startsWith("file:/")) {
        source = Paths.get(new URI(currentAssetPath)); // If it's a URI
      } else {
        source = Paths.get(currentAssetPath); // If it's a regular file path
      }

      String fileName = source.getFileName().toString();
      Path destination = assetsFolder.resolve(fileName);
      Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      LoggingManager.LOGGER.warn("Failed to write asset: {}", currentAssetPath, e);
      throw new ConfigException("Failed to copy assets when creating game file", e);
    } catch (URISyntaxException e) {
      LoggingManager.LOGGER.warn("Failed to write asset due to URI syntax error: {}", currentAssetPath, e);
      throw new ConfigException("Failed to copy assets when creating game file", e);
    }
  }


}
