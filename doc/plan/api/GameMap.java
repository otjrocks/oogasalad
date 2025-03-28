package oogasalad;

import java.util.Iterator;

/**
 * The {@code GameMap} interface provides methods for managing the game's map, which consists of
 * various Entities.
 *
 * @author Owen Jennings
 * @see Entity
 */
public interface GameMap {
  // I used ChatGPT to assist in writing the methods of this interface
  /**
   * Adds an entity to the game map at a specific position.
   *
   * @param entity the entity to be added to the map. Note, the entity interface will store the x
   *               and y value where the entity is located on the map.
   * @throws InvalidPositionException if the entity's position is invalid (e.g., occupied by another
   *                                  entity or outside the map).
   */
  void addEntity(Entity entity) throws InvalidPositionException;

  /**
   * Removes an entity from the map. If the specified Entity is not found on the map, throw an
   * EntityNotFoundException
   *
   * @param entity the entity to be removed from the map.
   * @throws EntityNotFoundException if the entity is not found on the map.
   */
  void removeEntity(Entity entity) throws EntityNotFoundException;

  /**
   * Retrieves an entity from the map based on its coordinate location.
   *
   * @param x the X-coordinate of the entity.
   * @param y the Y-coordinate of the entity.
   * @return the entity located at the specified position.
   * @throws EntityNotFoundException if no entity exists at the given coordinates.
   */
  Entity getEntityAt(int x, int y) throws EntityNotFoundException;

  /**
   * Returns an iterator for traversing the entities on the map. This iterator intentionally does
   * not reveal the data structure used to implement the GameMap, to allow for various structures,
   * such as Lists, 2D arrays, Maps, etc, based on the specific needs of a concrete implementation
   * of this interface.
   *
   * @return an iterator to iterate over all entities in the GameMap.
   */
  Iterator<Entity> iterator();

  /*
  Skeleton classes to ensure that class compiles correctly.
   */
  class Entity {

    Entity(int x, int y) {

    }
  }

  class InvalidPositionException extends Exception {

  }

  class EntityNotFoundException extends Exception {

  }
}
