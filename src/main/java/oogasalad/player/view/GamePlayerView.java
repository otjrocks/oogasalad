package oogasalad.player.view;

import static oogasalad.engine.config.GameConfig.WIDTH;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.StackPane;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.config.TileMapParser;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.api.GameMapFactory;
import oogasalad.engine.model.exceptions.InvalidPositionException;

public class GamePlayerView extends StackPane {

  private final MainController myMainController;
  private final GameState myGameState;
  private GameView myGameView; // ✅ stored

  public GamePlayerView(MainController controller, GameState gameState) {
    super();
    myMainController = controller;
    myGameState = gameState;

    this.setPrefWidth(WIDTH);
    this.getStyleClass().add("game-player-view");

    createExampleMap();
  }

  private void createExampleMap() {
    JsonConfigParser configParser = new JsonConfigParser();
    ConfigModel configModel = null;

    try {
      configModel = configParser.loadFromFile("data/basic.json");
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn("Failed to load configuration file: ", e);
    }

    GameMap gameMap = null;

    try {
      if (configModel != null) {
        gameMap = GameMapFactory.createGameMap(myMainController.getInputManager(), configModel);
        if (configModel.tiles() != null && !configModel.tiles().isEmpty()) {
          parseTilesToGameMap(configModel, gameMap);
        }
      }
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn("Failed to create or populate GameMap: ", e);
    }

    if (gameMap != null) {
      myGameView = new GameView(gameMap, myGameState);
      this.getChildren().add(myGameView);
    }
  }

  private void parseTilesToGameMap(ConfigModel configModel, GameMap gameMap)
      throws InvalidPositionException {

    String[] layout = configModel.tiles().getFirst().getLayout();
    TileMapParser tileParser = new TileMapParser();

    Map<String, EntityPlacement> templateMap = new HashMap<>();
    for (EntityPlacement data : configModel.entityPlacements()) {
      templateMap.put(data.getType().type(), data);
    }

    tileParser.parseTiles(layout, myMainController.getInputManager(), gameMap, templateMap);
  }

  public GameView getGameView() {
    return myGameView;
  }
}
