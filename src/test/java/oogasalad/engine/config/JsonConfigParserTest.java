package oogasalad.engine.config;

import java.nio.file.Path;
import oogasalad.engine.records.config.CollisionConfig;
import oogasalad.engine.records.config.EntityConfig;
import oogasalad.engine.records.config.GameConfig;
import oogasalad.engine.records.config.ImageConfig;
import oogasalad.engine.records.config.model.EntityProperties;
import oogasalad.engine.records.config.model.Level;
import oogasalad.engine.records.config.model.Metadata;
import oogasalad.engine.records.config.model.Settings;
import oogasalad.engine.records.config.model.wincondition.SurviveForTimeCondition;
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
                 "scoreStrategy": "Cumulative",
                 "winCondition": {
                   "type": "SurviveForTime",
                   "amount": 5
                 }
               },
               "levels": [
                 {
                   "levelMap": "level1"
                 }
               ],
               "collisions": [
                 {
                   "entityA": "pacman",
                   "modeA": [
                     0
                   ],
                   "entityB": "wall",
                   "eventsA": [
                     {
                       "type": "Stop"
                     }
                   ],
                   "eventsB": []
                 },
                 {
                   "entityA": "pacman",
                   "modeA": [
                     1
                   ],
                   "entityB": "blueghost",
                   "eventsA": [
                     {
                       "type": "UpdateScore",
                       "amount": 200
                     }
                   ],
                   "eventsB": [
                     {
                       "type": "Consume"
                     }
                   ]
                 },
                 {
                   "entityA": "pacman",
                   "modeA": [
                     1
                   ],
                   "entityB": "dot",
                   "eventsA": [
                     {
                       "type": "UpdateScore",
                       "amount": 10
                     }
                   ],
                   "eventsB": [
                     {
                       "type": "Consume"
                     }
                   ]
                 },
                 {
                   "entityA": "pacman",
                   "modeA": [
                     1
                   ],
                   "entityB": "cherry",
                   "eventsA": [
                     {
                       "type": "UpdateScore",
                       "amount": 100
                     }
                   ],
                   "eventsB": [
                     {
                       "type": "Consume"
                     }
                   ]
                 },
                 {
                   "entityA": "pacman",
                   "modeA": [
                     1
                   ],
                   "entityB": "strawberry",
                   "eventsA": [
                     {
                       "type": "UpdateScore",
                       "amount": 300
                     }
                   ],
                   "eventsB": [
                     {
                       "type": "Consume"
                     }
                   ]
                 },
                 {
                   "entityA": "pacman",
                   "modeA": [
                     1
                   ],
                   "entityB": "redghost",
                   "eventsA": [
                     {
                       "type": "UpdateLives",
                       "amount": -1
                     },
                     {
                       "type": "ReturnToSpawnLocation"
                     }
                   ],
                   "eventsB": [
                   ]
                 },
                 {
                   "entityA": "redghost",
                   "modeA": [
                     1
                   ],
                   "entityB": "pacman",
                   "eventsA": [
                     {
                       "type": "ReturnToSpawnLocation"
                     }
                   ],
                   "eventsB": [
                   ]
                 },
                 {
                   "entityA": "redghost",
                   "modeA": [
                     1
                   ],
                   "entityB": "wall",
                   "eventsA": [
                     {
                       "type": "Stop"
                     }
                   ],
                   "eventsB": [
                   ]
                 },
                 {
                   "entityA": "blueghost",
                   "modeA": [
                     1
                   ],
                   "entityB": "wall",
                   "eventsA": [
                     {
                       "type": "Stop"
                     }
                   ],
                   "eventsB": [
                   ]
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
    assertEquals("Cumulative", defaultSettings.scoreStrategy());
    assertEquals(new SurviveForTimeCondition(5), defaultSettings.winCondition());

    // Levels check
    List<Level> levels = config.levels();
    assertEquals(1, levels.size());

    assertEquals(1.5, defaultSettings.gameSpeed());
    assertEquals(3, defaultSettings.startingLives());
    assertEquals(0, defaultSettings.initialScore());

    // Collision check
    List<CollisionConfig> collisions = config.collisions();
    assertNotNull(collisions);
    assertEquals(9, collisions.size());

    // Folder path
    assertEquals(Path.of(tempDir.getPath()).normalize(),
        Path.of(config.gameFolderPath()).normalize());
  }

  @Test
  void loadEntityConfig_validEntityConfig_parsesCorrectly() throws IOException, ConfigException {
    // written by chatGPT

    // Arrange: Entity JSON with base and one overriding mode
    String json = """
        {
             "entityType": {
               "name": "RedGhost",
               "controlConfig": {
                 "controlStrategy": "Target",
                 "pathFindingStrategy": "Euclidean",
                 "targetCalculationConfig": {
                   "targetCalculationStrategy": "TargetEntity",
                   "targetType": "Pacman"
                 }
               },
               "movementSpeed": 90
             },
             "modes": [
               {
                 "name": "Default",
                 "image": {
                   "imagePath": "redghost",
                   "tileWidth": 14,
                   "tileHeight": 14,
                   "tilesToCycle": [
                     0,
                     1,
                     2,
                     3
                   ],
                   "animationSpeed": 1.0
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
    assertEquals("RedGhost", config.name());

    // Default properties
    EntityProperties baseProps = config.entityProperties();
    assertEquals("RedGhost", baseProps.name());
    assertEquals(90.0, baseProps.movementSpeed());

    // Modes
    List<ModeConfig> modes = config.modes();
    assertEquals(1, modes.size());

    ModeConfig defaultMode = modes.getFirst();
    assertEquals("Default", defaultMode.name());
    assertEquals("Default", defaultMode.entityProperties().name()); // now mode name

    ImageConfig defaultImg = defaultMode.image();
    assertEquals("redghost", defaultImg.imagePath());
    assertEquals(14, defaultImg.tileWidth());
    assertEquals(List.of(0, 1, 2, 3), defaultImg.tilesToCycle());

  }

  @Test
  void loadGameConfig_invalidFile_throwConfigException() throws IOException {
    // Note, the json file is missing a lot of required fields (has only metadata) so this should throw an error.
    String json = """
        {
               "metadata": {
                 "gameTitle": "Test Game",
                 "author": "Alice",
                 "gameDescription": "A sample test game"
               }
        }
        """;
    File entityFile = new File(tempDir, "invalid.json");
    Files.writeString(entityFile.toPath(), json);

    JsonConfigParser parser = new JsonConfigParser();
    assertThrows(ConfigException.class,
        () -> parser.loadEntityConfig(entityFile.getAbsolutePath()));
  }

}
