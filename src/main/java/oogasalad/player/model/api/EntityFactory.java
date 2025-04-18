package oogasalad.player.model.api;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.player.controller.GameInputManager;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;

/**
 * A factory design pattern used to create various entities.
 *
 * @author Jessica Chen
 */
public class EntityFactory {

  /**
   * Create an entity with the provided parameters.
   *
   * @param data The entity data for the entity that you wish to create.
   * @return An Entity object.
   * @see Entity
   */
  public static Entity createEntity(GameInputManager input, EntityPlacement data, GameMapInterface gameMap) {
    return new Entity(input, data, gameMap);
  }
}