package oogasalad.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import oogasalad.engine.config.api.ConfigParser;

/**
 * The JsonConfigParser class is responsible for parsing configuration files in JSON format and
 * converting them into ConfigModel objects. It uses the Jackson ObjectMapper for JSON
 * deserialization.
 *
 * <p>This class implements the ConfigParser interface and provides a method to load a
 * configuration from a file.
 *
 * <p>Example usage:
 *
 * <pre>
 * JsonConfigParser parser = new JsonConfigParser();
 * ConfigModel config = parser.loadFromFile("path/to/config.json");
 * </pre>
 *
 * @author Will He
 * @see ConfigParser
 * @see ConfigModel
 */
public class JsonConfigParser implements ConfigParser {

  private final ObjectMapper mapper;

  /**
   * Constructs a new JsonConfigParser instance. Initializes the Jackson ObjectMapper used for JSON
   * deserialization.
   */
  public JsonConfigParser() {
    this.mapper = new ObjectMapper();
  }

  public ConfigModel loadFromFile(String filepath) throws ConfigException {
    try {
      return mapper.readValue(new File(filepath), ConfigModel.class);
    } catch (IOException e) {
      throw new ConfigException("Failed to parse config file: " + filepath, e);
    }
  }
}
