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
import oogasalad.engine.records.GameContext;
import oogasalad.player.view.GameMapView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GameMapControllerTest {

  private GameMap mockGameMap;
  private GameState mockGameState;
  private GameMapView mockGameView;
  private GameContext gameContext;
  private GameMapController controller;

  @BeforeEach
  public void setUp() {
    mockGameMap = mock(GameMap.class);
    mockGameState = mock(GameState.class);
    mockGameView = mock(GameMapView.class);
    gameContext = new GameContext(mockGameMap, mockGameState);
    ConfigModel mockConfigModel = mock(ConfigModel.class);
    controller = new GameMapController(gameContext, mockConfigModel);
    controller = new GameMapController(gameContext, Mockito.mock(ConfigModel.class));
  }

  @Test
  public void updateEntityModels_setVelocityForEntity_entityPositionUpdates() throws Exception {
    EntityType type = new EntityType("SomeEntity", "wall", null, null, null, null);
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

