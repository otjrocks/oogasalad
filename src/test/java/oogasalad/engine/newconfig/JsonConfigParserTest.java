package oogasalad.engine.newconfig;

import oogasalad.engine.config.ConfigException;
import oogasalad.engine.newconfig.model.EntityProperties;
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
  void loadGameConfig_validGameConfig_parsesCorrectly() throws IOException, ConfigException {
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

  @Test
  void loadEntityConfig_validEntityConfig_parsesCorrectly() throws IOException, ConfigException {
    // written by chatGPT

    // Arrange: Entity JSON with base and one overriding mode
    String json = """
      {
        "entityType": {
          "name": "Ghost",
          "controlType": {
            "controlType": "Chase",
            "controlTypeConfig": {
              "targetType": "Player",
              "tilesAhead": 2
            }
          },
          "movementSpeed": 1.5,
          "blocks": ["Player"]
        },
        "modes": [
          {
            "name": "Frightened",
            "controlType": {
              "controlType": "Random"
            },
            "movementSpeed": 1.0,
            "image": {
              "imagePath": "ghost.png",
              "tileWidth": 32,
              "tileHeight": 32,
              "tilesToCycle": [0,1,2],
              "animationSpeed": 0.5
            }
          },
          {
            "name": "Normal",
            "image": {
              "imagePath": "ghost.png",
              "tileWidth": 32,
              "tileHeight": 32,
              "tilesToCycle": [0,1],
              "animationSpeed": 0.25
            }
          }
        ]
      }
    """;

    File entityFile = new File(tempDir, "ghost.json");
    Files.writeString(entityFile.toPath(), json);

    JsonConfigParser parser = new JsonConfigParser();
    EntityConfig config = parser.loadEntityConfig(entityFile.getAbsolutePath());

    // Act & Assert
    assertEquals("Ghost", config.name());

    // Default properties
    EntityProperties baseProps = config.entityProperties();
    assertEquals("Ghost", baseProps.name());
    assertEquals("Chase", baseProps.controlType().controlType());
    assertEquals("Player", baseProps.controlType().controlTypeConfig().targetType());
    assertEquals(2, baseProps.controlType().controlTypeConfig().tilesAhead());
    assertEquals(1.5, baseProps.movementSpeed());
    assertEquals(List.of("Player"), baseProps.blocks());

    // Modes
    List<ModeConfig> modes = config.modes();
    assertEquals(2, modes.size());

    ModeConfig frightened = modes.get(0);
    assertEquals("Frightened", frightened.name());
    assertEquals("Random", frightened.entityProperties().controlType().controlType());
    assertEquals(1.0, frightened.entityProperties().movementSpeed());
    assertEquals("Frightened", frightened.entityProperties().name()); // now mode name
    assertEquals(List.of("Player"), frightened.entityProperties().blocks()); // inherited

    ImageConfig frightImg = frightened.image();
    assertEquals("ghost.png", frightImg.imagePath());
    assertEquals(32, frightImg.tileWidth());
    assertEquals(List.of(0, 1, 2), frightImg.tilesToCycle());

    ModeConfig normal = modes.get(1);
    assertEquals("Normal", normal.name());
    assertEquals("Chase", normal.entityProperties().controlType().controlType()); // inherited
    assertEquals(1.5, normal.entityProperties().movementSpeed()); // inherited
    assertEquals("Normal", normal.entityProperties().name());
    assertEquals(List.of("Player"), normal.entityProperties().blocks()); // inherited
    assertEquals(List.of(0, 1), normal.image().tilesToCycle());
  }

}
