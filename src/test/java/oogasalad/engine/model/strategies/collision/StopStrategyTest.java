package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class StopStrategyTest {

  private StopStrategy strategy;
  private Entity entity1;
  private Entity entity2;
  private EntityPlacement placement1;
  private EntityPlacement placement2;
  private CollisionContext context;

  @BeforeEach
  void setUp() {
    strategy = new StopStrategy();
    entity1 = mock(Entity.class);
    entity2 = mock(Entity.class);
    placement1 = mock(EntityPlacement.class);
    placement2 = mock(EntityPlacement.class);
    context = mock(CollisionContext.class);

    when(context.entity1()).thenReturn(entity1);
    when(context.entity2()).thenReturn(entity2);
    when(entity1.getEntityPlacement()).thenReturn(placement1);
    when(entity2.getEntityPlacement()).thenReturn(placement2);
  }

  // again using verify because the stuff that should change is mocked
  // so if you chage what happens within the call, its kinda an oof

  @Test
  void handleCollision_entity1ToLeft_movesLeftAndStopsDirection() throws EntityNotFoundException {
    when(placement1.getX()).thenReturn(5.0);
    when(placement1.getY()).thenReturn(5.0);
    when(placement2.getX()).thenReturn(4.0);
    when(placement2.getY()).thenReturn(5.0);

    strategy.handleCollision(context);

    // I hope I did math right
    // pacman is 1, wall is 2 and current pacmanX > wall X
    // thus set wallX + 1 so set 5
    verify(placement1).setX(5.0);
  }

  @Test
  void handleCollision_samePosition_noMovementAndStopsDirection() throws EntityNotFoundException {
    when(placement1.getX()).thenReturn(3.0);
    when(placement1.getY()).thenReturn(3.0);
    when(placement2.getX()).thenReturn(3.0);
    when(placement2.getY()).thenReturn(3.0);

    strategy.handleCollision(context);

    verify(placement1, never()).setX(anyDouble());
    verify(placement1, never()).setY(anyDouble());
  }
}
