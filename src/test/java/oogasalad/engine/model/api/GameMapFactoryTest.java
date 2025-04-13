package oogasalad.engine.model.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameSettings;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.records.newconfig.model.ParsedLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class GameMapFactoryTest {

  private ConfigModel configModel;
  private GameInputManager inputManager;
  private EntityPlacement entityPlacement1;
  private EntityPlacement entityPlacement2;
  private Entity entity1;
  private Entity entity2;
  private GameSettings settings;

  @BeforeEach
  void setUp() {
    configModel = mock(ConfigModel.class);
    inputManager = mock(GameInputManager.class);
    entityPlacement1 = mock(EntityPlacement.class);
    entityPlacement2 = mock(EntityPlacement.class);
    entity1 = mock(Entity.class);
    entity2 = mock(Entity.class);
    settings = mock(GameSettings.class);
  }

  @Test
  void createGameMap_validConfig_createsMapSuccessfully() throws InvalidPositionException {
    when(settings.width()).thenReturn(10);
    when(settings.height()).thenReturn(10);
    when(configModel.settings()).thenReturn(settings);

    when(entity1.getEntityPlacement()).thenReturn(entityPlacement1);
    when(entity2.getEntityPlacement()).thenReturn(entityPlacement2);

    when(entityPlacement1.getX()).thenReturn(1.0);
    when(entityPlacement1.getY()).thenReturn(1.0);
    when(entityPlacement2.getX()).thenReturn(2.0);
    when(entityPlacement2.getY()).thenReturn(2.0);

    // Mock level and placements
    List<ParsedLevel> levels = new ArrayList<>();
    levels.add(new ParsedLevel(List.of(entityPlacement1, entityPlacement2), null, null));
    when(configModel.levels()).thenReturn(levels);

    // ChatGPT on how to mock entity factory correctly
    try (MockedStatic<EntityFactory> mockedFactory = mockStatic(EntityFactory.class)) {
      mockedFactory.when(() -> EntityFactory.createEntity(eq(inputManager), eq(entityPlacement1),
              any(GameMap.class)))
          .thenReturn(entity1);
      mockedFactory.when(() -> EntityFactory.createEntity(eq(inputManager), eq(entityPlacement2),
              any(GameMap.class)))
          .thenReturn(entity2);

      GameMap gameMap = GameMapFactory.createGameMap(inputManager, configModel, 0);

      assertNotNull(gameMap);
      assertTrue(gameMap.getEntityAt(1, 1).isPresent());
      assertTrue(gameMap.getEntityAt(2, 2).isPresent());
      assertEquals(gameMap.getEntityAt(1, 1).get(), entity1);
      assertEquals(gameMap.getEntityAt(2, 2).get(), entity2);
    }
  }

  @Test
  void createGameMap_invalidConfig_throwsException() {
    when(settings.width()).thenReturn(10);
    when(settings.height()).thenReturn(10);
    when(configModel.settings()).thenReturn(settings);

    when(entity1.getEntityPlacement()).thenReturn(entityPlacement1);
    when(entity2.getEntityPlacement()).thenReturn(entityPlacement2);

    when(entityPlacement1.getX()).thenReturn(-1.0);
    when(entityPlacement1.getY()).thenReturn(1.0);
    when(entityPlacement2.getX()).thenReturn(2.0);
    when(entityPlacement2.getY()).thenReturn(2.0);

    // Mock level and placements
    List<ParsedLevel> levels = new ArrayList<>();
    levels.add(new ParsedLevel(List.of(entityPlacement1, entityPlacement2), null, null));
    when(configModel.levels()).thenReturn(levels);

    // ChatGPT on how to mock entity factory correctly
    try (MockedStatic<EntityFactory> mockedFactory = mockStatic(EntityFactory.class)) {
      mockedFactory.when(() -> EntityFactory.createEntity(eq(inputManager), eq(entityPlacement1),
              any(GameMap.class)))
          .thenReturn(entity1);
      mockedFactory.when(() -> EntityFactory.createEntity(eq(inputManager), eq(entityPlacement2),
              any(GameMap.class)))
          .thenReturn(entity2);

      assertThrows(InvalidPositionException.class,
          () -> GameMapFactory.createGameMap(inputManager, configModel, 0));
    }
  }

  @Test
  void createGameMap_emptyEntityList_createsEmptyMap() throws InvalidPositionException {
    when(settings.width()).thenReturn(5);
    when(settings.height()).thenReturn(5);
    when(configModel.settings()).thenReturn(settings);
    // Mock level and placements
    List<ParsedLevel> levels = new ArrayList<>();
    levels.add(mock(ParsedLevel.class));
    when(configModel.levels()).thenReturn(levels);

    GameMap gameMap = GameMapFactory.createGameMap(inputManager, configModel, 0);

    assertNotNull(gameMap);
    for (int x = 0; x < 5; x++) {
      for (int y = 0; y < 5; y++) {
        assertTrue(gameMap.getEntityAt(x, y).isEmpty());
      }
    }
  }


}
