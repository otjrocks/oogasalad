package oogasalad.engine.config;

import java.nio.file.Path;
import oogasalad.engine.records.config.CollisionConfigRecord;
import oogasalad.engine.records.config.EntityConfigRecord;
import oogasalad.engine.records.config.GameConfigRecord;
import oogasalad.engine.records.config.ImageConfigRecord;
import oogasalad.engine.records.config.model.EntityPropertiesRecord;
import oogasalad.engine.records.config.model.LevelRecord;
import oogasalad.engine.records.config.model.MetadataRecord;
import oogasalad.engine.records.config.model.SettingsRecord;
import oogasalad.engine.records.config.model.wincondition.SurviveForTimeConditionRecord;
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
                   "modeA": "A",
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
                   "modeA": "A",
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
                   "modeA": "A",
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
                   "modeA": "A",
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
                   "modeA": "A",
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
                   "modeA": "A",
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
                   "modeA": "A",
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
                   "modeA": "A",
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
                   "modeA": "A",
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
    GameConfigRecord config = parser.loadGameConfig(configFile.getAbsolutePath());

    // Assert
    assertNotNull(config);

    // Metadata checks
    MetadataRecord metadata = config.metadata();
    assertEquals("Test Game", metadata.gameTitle());
    assertEquals("Alice", metadata.author());
    assertEquals("A sample test game", metadata.gameDescription());

    // Default settings check
    SettingsRecord defaultSettings = config.settings();
    assertEquals(1.5, defaultSettings.gameSpeed());
    assertEquals(3, defaultSettings.startingLives());
    assertEquals(0, defaultSettings.initialScore());
    assertEquals("Cumulative", defaultSettings.scoreStrategy());
    assertEquals(new SurviveForTimeConditionRecord(5), defaultSettings.winCondition());

    // Levels check
    List<LevelRecord> levels = config.levels();
    assertEquals(1, levels.size());

    assertEquals(1.5, defaultSettings.gameSpeed());
    assertEquals(3, defaultSettings.startingLives());
    assertEquals(0, defaultSettings.initialScore());

    // Collision check
    List<CollisionConfigRecord> collisions = config.collisions();
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
                   "tilesToCycle": 1,
                   "animationSpeed": 1.0
                 }
               }
             ]
           }
        """;

    File entityFile = new File(tempDir, "ghost.json");
    Files.writeString(entityFile.toPath(), json);

    JsonConfigParser parser = new JsonConfigParser();
    EntityConfigRecord config = parser.loadEntityConfig(entityFile.getAbsolutePath());

    // Act & Assert
    assertEquals("RedGhost", config.name());

    // Default properties
    EntityPropertiesRecord baseProps = config.entityProperties();
    assertEquals("RedGhost", baseProps.name());
    assertEquals(90.0, baseProps.movementSpeed());

    // Modes
    List<ModeConfigRecord> modes = config.modes();
    assertEquals(1, modes.size());

    ModeConfigRecord defaultMode = modes.getFirst();
    assertEquals("Default", defaultMode.name());
    assertEquals("Default", defaultMode.entityProperties().name()); // now mode name

    ImageConfigRecord defaultImg = defaultMode.image();
    assertEquals("redghost", defaultImg.imagePath());
    assertEquals(14, defaultImg.tileWidth());
    assertEquals(1, defaultImg.tilesToCycle());

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
