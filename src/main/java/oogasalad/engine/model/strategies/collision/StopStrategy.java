package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;

public class StopStrategy implements CollisionStrategy {

  @Override
  public void handleCollision(Entity pacman, Entity wall, GameMap gameMap, GameState gameState)
      throws EntityNotFoundException {
    double pacmanX = pacman.getEntityPlacement().getX();
    double pacmanY = pacman.getEntityPlacement().getY();
    double wallX = wall.getEntityPlacement().getX();
    double wallY = wall.getEntityPlacement().getY();

    if (pacmanX > wallX) {
      pacman.getEntityPlacement().setX(wall.getEntityPlacement().getX() + 1);
    }
    if (pacmanX < wallX) {
      pacman.getEntityPlacement().setX(wall.getEntityPlacement().getX() - 1);
    }
    if (pacmanY > wallY) {
      pacman.getEntityPlacement().setY(wall.getEntityPlacement().getY() + 1);
    }
    if (pacmanY < wallY) {
      pacman.getEntityPlacement().setY(wall.getEntityPlacement().getY() - 1);
    }
    pacman.setEntityDirection(' ');
  }
}
