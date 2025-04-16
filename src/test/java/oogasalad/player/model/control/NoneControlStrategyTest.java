package oogasalad.player.model.control;

import static org.mockito.Mockito.*;

import oogasalad.engine.model.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NoneControlStrategyTest {

  private Entity entity;
  private ControlStrategyInterface strategy;

  @BeforeEach
  void setup() {
    entity = mock(Entity.class);
    strategy = new oogasalad.player.model.control.testdoubles.NoneControlStrategy();
  }

  @Test
  void update_doesNothing_noInteractionWithEntity() {
    strategy.update(entity);
    verifyNoInteractions(entity);
  }
}
