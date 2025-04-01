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
import oogasalad.engine.model.api.GameMapFactory;
import oogasalad.engine.model.exceptions.InvalidPositionException;

/**
 * The view that displays only the game grid.
 *
 * @author Owen Jennings
 */
public class GamePlayerView extends StackPane {

  private final MainController myMainController;

  /**
   * Create the Game Player View.
   */
  public GamePlayerView(MainController controller) {
    super();
    myMainController = controller;

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

        if (configModel.getTiles() != null && !configModel.getTiles().isEmpty()) {
          parseTilesToGameMap(configModel, gameMap);
        }
      }
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn("Failed to create or populate GameMap: ", e);
    }

    if (gameMap != null) {
      GameView gameView = new GameView(gameMap);
      this.getChildren().add(gameView);
    }
  }

  /**
   * Parses tile layout from config and adds entities to the game map.
   *
   * @param configModel the loaded config data
   * @param gameMap the game map to populate
   * @throws InvalidPositionException if an entity cannot be added to the map
   */
  private void parseTilesToGameMap(ConfigModel configModel, GameMap gameMap)
      throws InvalidPositionException {

    String[] layout = configModel.getTiles().get(0).getLayout();
    TileMapParser tileParser = new TileMapParser();

    Map<String, EntityPlacement> templateMap = new HashMap<>();
    for (EntityPlacement data : configModel.getEntityPlacements()) {
      templateMap.put(data.getType().getType(), data);
    }

    tileParser.parseTiles(layout, myMainController.getInputManager(), gameMap, templateMap);
  }
}
