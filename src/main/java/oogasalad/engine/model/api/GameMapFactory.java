package oogasalad.engine.model.api;

import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.model.Entity;
import oogasalad.engine.model.EntityData;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameMapImpl;
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
   * @return A game map object
   * @throws InvalidPositionException Whenever the map cannot be created because an entity with an
   *                                  invalid position was added.
   */
  public GameMap createGameMap(ConfigModel configModel) throws InvalidPositionException {
    GameMapImpl gameMap = new GameMapImpl(1000, 1000); // Hardcoded for now
    for (EntityData entityData : configModel.getEntityConfigs()) {
      Entity entity = new Entity(entityData);
      gameMap.addEntity(entity);
    }
    return gameMap;
  }
}
