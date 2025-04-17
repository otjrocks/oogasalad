package oogasalad.player.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import oogasalad.engine.config.ConfigModelRecord;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityTypeRecord;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.engine.model.GameStateInterface;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.model.losecondition.LivesBasedConditionRecord;
import oogasalad.engine.records.config.model.wincondition.EntityBasedConditionRecord;
import oogasalad.player.view.GameMapView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameMapControllerTest {

  private GameMapInterface mockGameMap;
  private GameStateInterface mockGameState;
  private GameMapController controller;

  @BeforeEach
  public void setUp() {
    mockGameMap = mock(GameMapInterface.class);
    mockGameState = mock(GameStateInterface.class);
    GameMapView mockGameView = mock(GameMapView.class);
    GameContextRecord gameContext = new GameContextRecord(mockGameMap, mockGameState);

    ConfigModelRecord mockConfigModel = mock(ConfigModelRecord.class);
    when(mockConfigModel.winCondition()).thenReturn(new EntityBasedConditionRecord("dot")); // or any WinCondition
    when(mockConfigModel.loseCondition()).thenReturn(new LivesBasedConditionRecord());

    controller = new GameMapController(gameContext, mockConfigModel);
  }

  @Test
  public void updateEntityModels_setVelocityForEntity_entityPositionUpdates() throws Exception {
    EntityTypeRecord type = new EntityTypeRecord("SomeEntity", null, null, null, 1.0);
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

