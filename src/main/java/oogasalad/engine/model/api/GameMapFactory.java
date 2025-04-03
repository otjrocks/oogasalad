package oogasalad.engine.model.api;

import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameMapImpl;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.InvalidPositionException;

/**
 * A factory design pattern to create a GameMap provided a configuration model.
 *
 * @author Owen Jennings
 */
public class GameMapFactory {

  /**
   * Create a game map with the provided configuration model.
   *
   * @param configModel The configuration model you wish to use to create the game map.
   * @return A game map object
   * @throws InvalidPositionException Whenever the map cannot be created because an entity with an
   *                                  invalid position was added.
   */
  public static GameMap createGameMap(GameInputManager input, ConfigModel configModel)
      throws InvalidPositionException {
    int width = configModel.getSettings().getWidth();
    int height = configModel.getSettings().getHeight();
    GameMapImpl gameMap = new GameMapImpl(width, height); // Hardcoded for now
    for (EntityPlacement entityPlacement : configModel.getEntityPlacements()) {
      Entity entity = EntityFactory.createEntity(input, entityPlacement, gameMap);
      gameMap.addEntity(entity);
    }
    return gameMap;
  }
}
