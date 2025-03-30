package oogasalad.engine.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.exceptions.InvalidPositionException;

public class GameMapImpl implements GameMap {

  private final List<Entity> myEntityList;
  private final int myWidth;
  private final int myHeight;

  public GameMapImpl(int width, int height) {
    myEntityList = new ArrayList<>();
    myWidth = width;
    myHeight = height;
  }

  @Override
  public void addEntity(Entity entity) throws InvalidPositionException {
    EntityData data = entity.entityData();
    checkEntryInBounds(entity, data);
    myEntityList.add(entity);
  }

  private void checkEntryInBounds(Entity entity, EntityData data) throws InvalidPositionException {
    if (data.getInitialX() < 0 || data.getInitialX() >= myWidth || data.getInitialY() < 0
        || data.getInitialY() >= myHeight) {
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
  public Entity getEntityAt(int x, int y) throws EntityNotFoundException {
    for (Entity entity : myEntityList) {
      if (entity.entityData().getInitialX() == x && entity.entityData().getInitialY() == y) {
        return entity;
      }
    }
    throw new EntityNotFoundException("The entity you provided does not exist in the game map!");
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
}
