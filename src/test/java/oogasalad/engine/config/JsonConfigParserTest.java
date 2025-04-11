package oogasalad.engine.config;

import oogasalad.engine.records.newconfig.CollisionConfig;
import oogasalad.engine.records.newconfig.EntityConfig;
import oogasalad.engine.records.newconfig.GameConfig;
import oogasalad.engine.records.newconfig.ImageConfig;
import oogasalad.engine.records.newconfig.model.EntityProperties;
import oogasalad.engine.records.newconfig.model.Level;
import oogasalad.engine.records.newconfig.model.Metadata;
import oogasalad.engine.records.newconfig.model.Settings;
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
          ],
          "collisions": [
            {
              "entityA": "Player",
              "modeA": [1, 2],
              "entityB": "Enemy",
              "modeB": [3],
              "eventsA": ["LoseLife", "Flash"],
              "eventsB": ["BounceBack"]
            },
            {
              "entityA": "Player",
              "entityB": "Coin",
              "eventsA": ["IncreaseScore"],
              "eventsB": []
            }
          ]
        }
        """;

    // Create a temp JSON file
    File configFile = new File(tempDir, "gameConfig.json");
    Files.writeString(configFile.toPath(), json);

    JsonConfigParser parser = new JsonConfigParser();

    // Act
    GameConfig config = parser.loadGameConfig(configFile.getAbsolutePath());

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

    assertEquals(1.5, defaultSettings.gameSpeed());
    assertEquals(3, defaultSettings.startingLives());
    assertEquals(0, defaultSettings.initialScore());

    // Collision check
    List<CollisionConfig> collisions = config.collisions();
    assertNotNull(collisions);
    assertEquals(2, collisions.size());

    CollisionConfig c1 = collisions.get(0);
    assertEquals("Player", c1.entityA());
    assertEquals(List.of(1, 2), c1.modeA());
    assertEquals("Enemy", c1.entityB());
    assertEquals(List.of(3), c1.modeB());
    assertEquals(List.of("LoseLife", "Flash"), c1.eventsA());
    assertEquals(List.of("BounceBack"), c1.eventsB());

    CollisionConfig c2 = collisions.get(1);
    assertEquals("Player", c2.entityA());
    assertNull(c2.modeA()); // optional
    assertEquals("Coin", c2.entityB());
    assertNull(c2.modeB()); // optional
    assertEquals(List.of("IncreaseScore"), c2.eventsA());
    assertEquals(List.of(), c2.eventsB());

    // Folder path
    assertEquals(tempDir.getPath() + '/', config.gameFolderPath());
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
    List<oogasalad.engine.records.newconfig.ModeConfig> modes = config.modes();
    assertEquals(2, modes.size());

    oogasalad.engine.records.newconfig.ModeConfig frightened = modes.getFirst();
    assertEquals("Frightened", frightened.name());
    assertEquals("Random", frightened.entityProperties().controlType().controlType());
    assertEquals(1.0, frightened.entityProperties().movementSpeed());
    assertEquals("Frightened", frightened.entityProperties().name()); // now mode name
    assertEquals(List.of("Player"), frightened.entityProperties().blocks()); // inherited

    ImageConfig frightImg = frightened.image();
    assertEquals("ghost.png", frightImg.imagePath());
    assertEquals(32, frightImg.tileWidth());
    assertEquals(List.of(0, 1, 2), frightImg.tilesToCycle());

    oogasalad.engine.records.newconfig.ModeConfig normal = modes.get(1);
    assertEquals("Normal", normal.name());
    assertEquals("Chase", normal.entityProperties().controlType().controlType()); // inherited
    assertEquals(1.5, normal.entityProperties().movementSpeed()); // inherited
    assertEquals("Normal", normal.entityProperties().name());
    assertEquals(List.of("Player"), normal.entityProperties().blocks()); // inherited
    assertEquals(List.of(0, 1), normal.image().tilesToCycle());
  }

}
