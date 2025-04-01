package oogasalad.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import oogasalad.engine.config.api.ConfigParser;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;

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

  /**
   * Retrieve the config model from a file with the provided file path.
   *
   * @param filepath the path to the configuration file to be loaded
   * @return The config model data from this file.
   * @throws ConfigException If the config file cannot be parsed.
   */
  public ConfigModel loadFromFile(String filepath) throws ConfigException {
    try {
      ConfigModel model = mapper.readValue(new File(filepath), ConfigModel.class);
      resolveEntityTypes(model);
      return model;
    } catch (IOException e) {
      throw new ConfigException("Failed to parse config file: " + filepath, e);
    }
  }

  public void resolveEntityTypes(ConfigModel config) {
    Map<String, EntityType> typeMap = config.getEntityTypes().stream()
        .collect(Collectors.toMap(EntityType::getType, Function.identity()));

    for (EntityPlacement placement : config.getEntityPlacements()) {
      EntityType matchedType = typeMap.get(placement.getTypeString());
      if (matchedType == null) {
        throw new IllegalArgumentException("Unknown entity type: " + placement.getType());
      }
      placement.setResolvedEntityType(matchedType);
    }
  }
}
