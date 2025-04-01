package oogasalad.player.view;

import static oogasalad.engine.config.GameConfig.HEIGHT;
import static oogasalad.engine.config.GameConfig.WIDTH;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.Pane;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.config.TileMapParser;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.EntityData;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.api.GameMapFactory;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.model.GameState;

/**
 * The view that displays only the game grid.
 */
public class GamePlayerView extends Pane {

  private final MainController myMainController;
  private final GameState gameState;

  /**
   * Create the Game Player View.
   */
  public GamePlayerView(MainController controller, GameState gameState) {
    super();
    myMainController = controller;
    this.gameState = gameState;

    this.setPrefSize(WIDTH, HEIGHT);
    this.getStyleClass().add("game-player-view");

    createExampleMap();
  }

  private void createExampleMap() {
    ConfigModel configModel = loadConfigModel();
    if (configModel == null) return;

    GameMap gameMap = buildGameMap(configModel);
    if (gameMap == null) return;

    loadTilesIntoMap(configModel, gameMap);
    this.getChildren().add(new GameView(gameMap));
  }

  private ConfigModel loadConfigModel() {
    try {
      return new JsonConfigParser().loadFromFile("data/basic.json");
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn(e);
      return null;
    }
  }

  private GameMap buildGameMap(ConfigModel configModel) {
    try {
      return GameMapFactory.createGameMap(myMainController.getInputManager(), configModel, 20, 20);
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn(e);
      return null;
    }
  }

  private void loadTilesIntoMap(ConfigModel configModel, GameMap gameMap) {
    if (configModel.getTiles() == null || configModel.getTiles().isEmpty()) return;

    String[] layout = configModel.getTiles().get(0).getLayout();
    Map<String, EntityData> templateMap = buildTemplateMap(configModel);

    TileMapParser tileParser = new TileMapParser();
    try {
      tileParser.parseTiles(layout, myMainController.getInputManager(), gameMap, templateMap);
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn(e);
    }
  }

  private Map<String, EntityData> buildTemplateMap(ConfigModel configModel) {
    Map<String, EntityData> map = new HashMap<>();
    for (EntityData data : configModel.getEntityConfigs()) {
      map.put(data.getType(), data);
    }
    return map;
  }
}
