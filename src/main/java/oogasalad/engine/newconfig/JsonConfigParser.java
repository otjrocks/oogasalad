package oogasalad.engine.newconfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.newconfig.api.ConfigParser;
import oogasalad.engine.newconfig.model.Level;
import oogasalad.engine.newconfig.model.Metadata;
import oogasalad.engine.newconfig.model.Settings;

/**
 * The {@code JsonConfigParser} class is responsible for parsing game configuration files in JSON
 * format and converting them into {@link GameConfig} objects. It uses the Jackson library to handle
 * JSON parsing and mapping.
 *
 * <p>This class implements the {@link ConfigParser} interface and provides methods to
 * load configuration data from a file, merge settings, and extract folder paths.
 *
 * <p>Example usage:
 * <pre>
 * JsonConfigParser parser = new JsonConfigParser();
 * GameConfig config = parser.loadFromFile("path/to/config.json");
 * </pre>
 *
 * @author Jessica Chen
 */
public class JsonConfigParser implements ConfigParser {

  private final ObjectMapper mapper;

  public JsonConfigParser() {
    this.mapper = new ObjectMapper();
  }

  public GameConfig loadFromFile(String filepath) throws ConfigException {
    GameConfig gameConfig = loadGameConfig(filepath);

    String folderPath = gameConfig.gameFolderPath();

    return gameConfig;
  }

  private GameConfig loadGameConfig(String filepath) throws ConfigException {
    try {
      JsonNode root = mapper.readTree(new File(filepath));

      // Parse each section manually
      Metadata metadata = mapper.treeToValue(root.get("metadata"), Metadata.class);
      Settings defaultSettings = mapper.treeToValue(root.get("defaultSettings"), Settings.class);

      List<Level> levels = new ArrayList<>();
      for (JsonNode levelNode : root.get("levels")) {
        JsonNode settingsNode = levelNode.get("settings");
        Settings levelSettings = settingsNode != null
            ? mapper.treeToValue(settingsNode, Settings.class)
            : null;
        String levelMap = levelNode.get("levelMap").asText();
        Settings merged = mergeSettings(defaultSettings, levelSettings);
        levels.add(new Level(merged, levelMap));
      }

      return new GameConfig(metadata, defaultSettings, levels, getFolderPath(filepath));

    } catch (IOException e) {
      throw new ConfigException("Failed to parse config file: " + filepath, e);
    }
  }

  private Settings mergeSettings(Settings defaults, Settings override) {
    if (override == null) {
      return defaults;
    }

    return new Settings(
        override.gameSpeed() != null ? override.gameSpeed() : defaults.gameSpeed(),
        override.startingLives() != null ? override.startingLives() : defaults.startingLives(),
        override.initialScore() != null ? override.initialScore() : defaults.initialScore(),
        override.scoreStrategy() != null ? override.scoreStrategy() : defaults.scoreStrategy(),
        override.winCondition() != null ? override.winCondition() : defaults.winCondition()
    );
  }

  private String getFolderPath(String filepath) throws ConfigException {
    Pattern pattern = Pattern.compile("^(.*[\\\\/])");
    Matcher matcher = pattern.matcher(filepath);

    if (matcher.find()) {
      return matcher.group(1);
    }

    throw new ConfigException("Failed to getFolderPath: " + filepath);
  }


}
