package oogasalad.engine.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.exceptions.InvalidPositionException;

/**
 * A simple implementation of a {@code GameMap}.
 *
 * @author Owen Jennings
 */
public class GameMapImpl implements GameMap {

  private final List<Entity> myEntityList;
  private final int myWidth;
  private final int myHeight;

  /**
   * The constructor for a game map.
   *
   * @param width  The width in cells of this game map.
   * @param height The height in cells of this game map.
   */
  public GameMapImpl(int width, int height) {
    myEntityList = new ArrayList<>();
    myWidth = width;
    myHeight = height;
  }

  @Override
  public void addEntity(Entity entity) throws InvalidPositionException {
    EntityPlacement data = entity.getEntityPlacement();
    checkEntryInBounds(entity, data);
    myEntityList.add(entity);
  }

  private void checkEntryInBounds(Entity entity, EntityPlacement data)
      throws InvalidPositionException {
    if (data.getX() < 0 || data.getX() >= myWidth || data.getY() < 0
        || data.getY() >= myHeight) {
      LoggingManager.LOGGER.warn("Invalid entry position: {}, {}", data.getX(), data.getY());
      LoggingManager.LOGGER.warn("Cannot add entity {} because it does not fit in the game map!",
          entity);
      throw new InvalidPositionException("The entity you provided does not fit in this game map!");
    }
  }

  @Override
  public void removeEntity(Entity entity) throws EntityNotFoundException {
    if (!myEntityList.contains(entity)) {
      LoggingManager.LOGGER.warn("Cannot remove entity {} because it does not exist!", entity);
      throw new EntityNotFoundException(
          "Cannot remove requested entity, because it does not exist in the game map!");
    } else {
      myEntityList.remove(entity);
    }
  }

  @Override
  public Optional<Entity> getEntityAt(int x, int y) {
    for (Entity entity : myEntityList) {
      if (entity.getEntityPlacement().getX() == x && entity.getEntityPlacement().getY() == y) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }


  @Override
  public Iterator<Entity> iterator() {
    return myEntityList.iterator();
  }

  @Override
  public int getWidth() {
    return myWidth;
  }

  @Override
  public int getHeight() {
    return myHeight;
  }

  @Override
  public void update() {
    for (Entity entity : myEntityList) {
      entity.update();
    }
  }
}
