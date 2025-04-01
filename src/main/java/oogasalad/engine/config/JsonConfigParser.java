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
 * The {@code JsonConfigParser} class is responsible for parsing configuration files in JSON format
 * and converting them into {@link ConfigModel} objects. It uses the Jackson {@link ObjectMapper}
 * for JSON deserialization.
 *
 * <p>This class implements the {@link ConfigParser} interface and provides a method to load a
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
   * Constructs a new {@code JsonConfigParser} instance. Initializes the Jackson {@code ObjectMapper}
   * used for JSON deserialization.
   */
  public JsonConfigParser() {
    this.mapper = new ObjectMapper();
  }

  /**
   * Loads a {@link ConfigModel} from a JSON file at the specified file path.
   *
   * <p>After deserializing the file, this method also resolves the entity types of each
   * {@link EntityPlacement} by matching their type strings to defined {@link EntityType}s.
   *
   * @param filepath the path to the JSON configuration file to be loaded
   * @return the fully populated {@link ConfigModel} object
   * @throws ConfigException if the file is missing or cannot be parsed correctly
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

  /**
   * Resolves entity types for all {@link EntityPlacement}s in the given configuration by
   * matching their type strings to the list of defined {@link EntityType}s.
   *
   * <p>This method updates each {@code EntityPlacement} with the corresponding resolved
   * {@code EntityType}. If a placement refers to an undefined type, an exception is thrown.
   *
   * @param config the {@link ConfigModel} whose placements need to be resolved
   * @throws IllegalArgumentException if a referenced entity type is not found
   */
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
