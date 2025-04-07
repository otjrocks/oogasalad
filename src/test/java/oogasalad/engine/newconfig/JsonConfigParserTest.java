package oogasalad.engine.newconfig;

import oogasalad.engine.config.ConfigException;
import oogasalad.engine.newconfig.model.Level;
import oogasalad.engine.newconfig.model.Metadata;
import oogasalad.engine.newconfig.model.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonConfigParserTest {

  @TempDir
  File tempDir;

  @Test
  void testLoadFromFile_parsesValidConfigCorrectly() throws IOException, ConfigException {
    // this test was written by chatGPT

    // Arrange: Sample JSON content
    String json = """
          {
            "metadata": {
              "gameTitle": "Test Game",
              "author": "Alice",
              "gameDescription": "A sample test game"
            },
            "defaultSettings": {
              "gameSpeed": 1.5,
              "startingLives": 3,
              "initialScore": 0,
              "scoreStrategy": "Standard",
              "winCondition": "CollectAllItems"
            },
            "levels": [
              {
                "settings": {
                  "gameSpeed": 2.0
                },
                "levelMap": "level1.map"
              },
              {
                "levelMap": "level2.map"
              }
            ]
          }
        """;

    // Create a temp JSON file
    File configFile = new File(tempDir, "gameConfig.json");
    Files.writeString(configFile.toPath(), json);

    JsonConfigParser parser = new JsonConfigParser();

    // Act
    GameConfig config = parser.loadFromFile(configFile.getAbsolutePath());

    // Assert
    assertNotNull(config);

    // Metadata checks
    Metadata metadata = config.metadata();
    assertEquals("Test Game", metadata.gameTitle());
    assertEquals("Alice", metadata.author());
    assertEquals("A sample test game", metadata.gameDescription());

    // Default settings check
    Settings defaultSettings = config.settings();
    assertEquals(1.5, defaultSettings.gameSpeed());
    assertEquals(3, defaultSettings.startingLives());
    assertEquals(0, defaultSettings.initialScore());
    assertEquals("Standard", defaultSettings.scoreStrategy());
    assertEquals("CollectAllItems", defaultSettings.winCondition());

    // Levels check
    List<Level> levels = config.levels();
    assertEquals(2, levels.size());

    Settings level1 = levels.get(0).settings();
    assertEquals(2.0, level1.gameSpeed()); // Overridden
    assertEquals(3, level1.startingLives()); // From default
    assertEquals(0, level1.initialScore());

    Settings level2 = levels.get(1).settings();
    assertEquals(1.5, level2.gameSpeed()); // From default
    assertEquals(3, level2.startingLives());

    // Folder path
    assertEquals(tempDir.getPath()+'/', config.gameFolderPath());
  }
}
