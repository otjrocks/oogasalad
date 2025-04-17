package oogasalad.player.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.model.wincondition.EntityBasedCondition;
import oogasalad.player.view.GameMapView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameMapControllerTest {

  private GameMap mockGameMap;
  private GameState mockGameState;
  private GameMapController controller;

  @BeforeEach
  public void setUp() {
    mockGameMap = mock(GameMap.class);
    mockGameState = mock(GameState.class);
    GameMapView mockGameView = mock(GameMapView.class);
    GameContextRecord gameContext = new GameContextRecord(mockGameMap, mockGameState);

    ConfigModel mockConfigModel = mock(ConfigModel.class);
    when(mockConfigModel.winCondition()).thenReturn(new EntityBasedCondition("dot")); // or any WinCondition

    controller = new GameMapController(gameContext, mockConfigModel);
  }

  @Test
  public void updateEntityModels_setVelocityForEntity_entityPositionUpdates() throws Exception {
    EntityType type = new EntityType("SomeEntity", null, null, null, 1.0);
    EntityPlacement placement = new EntityPlacement(type, 5, 5, "Default");
    GameInputManager mockInputManager = mock(GameInputManager.class);
    Entity entity = new Entity(mockInputManager, placement, mockGameMap);
    entity.setDx(1);
    entity.setDy(-1);

    List<Entity> entities = List.of(entity);

    doReturn(entities.iterator()).when(mockGameMap).iterator();
    when(mockGameState.getScore()).thenReturn(0);

    controller.updateEntityModels();

    assertEquals(6, entity.getEntityPlacement().getX());
    assertEquals(4, entity.getEntityPlacement().getY());
  }
}

