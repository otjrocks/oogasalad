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
 * Handles loading a config file, creating the GameMap,
 * and populating it from tile data.
 *
 * Includes robust error handling and logging.
 *
 * @author Luke Fu
 */
public class GamePlayerView extends Pane {

  private final MainController myMainController;

  /**
   * Create the Game Player View.
   *
   * @param controller the main controller managing input and game state
   * @param gameState the shared game state (score, lives, etc.)
   */
  public GamePlayerView(MainController controller, GameState gameState) {
    super();
    myMainController = controller;

    this.setPrefSize(WIDTH, HEIGHT);
    this.getStyleClass().add("game-player-view");

    createExampleMap();
  }

  /**
   * Creates and loads the map based on JSON configuration,
   * applying tile layout and entity templates.
   */
  private void createExampleMap() {
    ConfigModel configModel = null;
    try {
      configModel = new JsonConfigParser().loadFromFile("data/basic.json");
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn("Failed to load configuration file: ", e);
    }

    GameMap gameMap = null;
    try {
      if (configModel != null) {
        gameMap = GameMapFactory.createGameMap(
            myMainController.getInputManager(), configModel, 20, 20);

        if (configModel.getTiles() != null && !configModel.getTiles().isEmpty()) {
          parseTilesToGameMap(configModel, gameMap);
        }
      }
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn("Failed to create or populate GameMap: ", e);
    }

    if (gameMap != null) {
      this.getChildren().add(new GameView(gameMap));
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

    Map<String, EntityData> templateMap = new HashMap<>();
    for (EntityData data : configModel.getEntityConfigs()) {
      templateMap.put(data.getType(), data);
    }

    tileParser.parseTiles(layout, myMainController.getInputManager(), gameMap, templateMap);
  }
}
