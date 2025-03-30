package oogasalad.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import oogasalad.engine.config.api.ConfigSaver;

/**
 * The JsonConfigSaver class is responsible for saving configuration data to JSON files. It uses the
 * Jackson ObjectMapper for JSON serialization and supports pretty-printing for better readability
 * of the output files.
 *
 * <p>This class implements the ConfigSaver interface and provides methods to save a ConfigModel to
 * a file and retrieve the supported file extensions.
 *
 * <p>Example usage:
 *
 * <pre>
 * JsonConfigSaver saver = new JsonConfigSaver();
 * saver.saveToFile(configModel, "path/to/config.json");
 * </pre>
 *
 * @author Will He
 * @see ConfigSaver
 * @see ConfigModel
 */
public class JsonConfigSaver implements ConfigSaver {

  private final ObjectMapper mapper;

  /**
   * Constructs a new JsonConfigSaver instance. Initializes the Jackson ObjectMapper used for JSON
   * serialization and enables pretty-printing for the output JSON files.
   */
  public JsonConfigSaver() {
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT); // makes it look nice
  }

  /**
   * Saves the provided ConfigModel to a file at the given path as JSON.
   *
   * @param config   the config model to save
   * @param filepath the full file path to save to (e.g., "data/configs/myGame.json")
   * @throws ConfigException if the file cannot be written
   */
  @Override
  public void saveToFile(ConfigModel config, String filepath) throws ConfigException {
    File file = new File(filepath);
    try {
      mapper.writeValue(file, config);
    } catch (IOException e) {
      throw new ConfigException("Failed to save config file to: " + filepath, e);
    }
  }

  /**
   * Returns a list of supported file extensions. In this implementation: only JSON.
   *
   * @return a list containing the supported file extension ".json"
   */
  @Override
  public List<String> getSupportedFileExtensions() {
    return Collections.singletonList(".json");
  }
}
