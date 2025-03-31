package oogasalad.engine.model.api;

import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.model.EntityData;
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
   * Create a game map with the provided configuration model
   *
   * @param configModel The configuration model you wish to use to create the game map.
   * @param width       The width of the game map.
   * @param height      The height of the game map.
   * @return A game map object
   * @throws InvalidPositionException Whenever the map cannot be created because an entity with an
   *                                  invalid position was added.
   */
  public static GameMap createGameMap(ConfigModel configModel, int width, int height)
      throws InvalidPositionException {
    GameMapImpl gameMap = new GameMapImpl(width, height); // Hardcoded for now
    for (EntityData entityData : configModel.getEntityConfigs()) {
      Entity entity = EntityFactory.createEntity(entityData);
      gameMap.addEntity(entity);
    }
    return gameMap;
  }
}
