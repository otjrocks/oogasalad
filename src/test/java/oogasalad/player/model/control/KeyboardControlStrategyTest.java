package oogasalad.player.model.control;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KeyboardControlStrategyTest {

  private GameInputManager input;
  private GameMap gameMap;
  private EntityPlacement placement;
  private Entity entity;
  private KeyboardControlStrategy strategy;

  @BeforeEach
  void setup() {
    input = mock(GameInputManager.class);
    gameMap = mock(GameMap.class);
    placement = mock(EntityPlacement.class);
    entity = mock(Entity.class);

    when(placement.getX()).thenReturn(5.0);
    when(placement.getY()).thenReturn(5.0);

    strategy = new KeyboardControlStrategy(input, gameMap, placement);
  }

  @Test
  void update_movingUpNoWall_setsDirectionUp() {
    when(input.isMovingUp()).thenReturn(true);
    when(entity.canMove(Direction.U)).thenReturn(true);
    when(gameMap.getEntityAt(5, 4)).thenReturn(Optional.empty());

    strategy.update(entity);

    verify(entity).setEntityDirection(Direction.U);
  }

  @Test
  void update_movingDownWthWall_doesNotSetDirectionDown() {
    when(input.isMovingDown()).thenReturn(true);
    when(entity.canMove(Direction.D)).thenReturn(true);

    Entity wallEntity = mock(Entity.class);
    EntityPlacement wallPlacement = mock(EntityPlacement.class);
    EntityType wallType = mock(EntityType.class);

    when(wallEntity.getEntityPlacement()).thenReturn(wallPlacement);
    when(wallPlacement.getType()).thenReturn(wallType);
    when(wallType.type()).thenReturn("Wall");

    when(gameMap.getEntityAt(5, 6)).thenReturn(Optional.of(wallEntity));

    strategy.update(entity);

    verify(entity, never()).setEntityDirection(Direction.D);
  }

  @Test
  void update_movingLeftCanMove_setsDirectionLeft() {
    when(input.isMovingLeft()).thenReturn(true);
    when(entity.canMove(Direction.L)).thenReturn(true);
    when(gameMap.getEntityAt(4, 5)).thenReturn(Optional.empty());

    strategy.update(entity);

    verify(entity).setEntityDirection(Direction.L);
  }

  @Test
  void update_movingRightCannotMove_doesNotSetDirection() {
    when(input.isMovingRight()).thenReturn(true);
    when(entity.canMove(Direction.R)).thenReturn(false);

    strategy.update(entity);

    verify(entity, never()).setEntityDirection(Direction.R);
  }

  @Test
  void update_noInputDoesNotSetAnyDirection() {
    when(input.isMovingUp()).thenReturn(false);
    when(input.isMovingDown()).thenReturn(false);
    when(input.isMovingLeft()).thenReturn(false);
    when(input.isMovingRight()).thenReturn(false);

    strategy.update(entity);

    verify(entity, never()).setEntityDirection(any(Direction.class));
  }
}
