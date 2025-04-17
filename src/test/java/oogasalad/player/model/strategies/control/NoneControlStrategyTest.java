package oogasalad.player.model.strategies.control;

import static org.mockito.Mockito.*;

import oogasalad.player.model.Entity;
import oogasalad.player.model.strategies.control.testdoubles.NoneControlStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NoneControlStrategyTest {

  private Entity entity;
  private ControlStrategyInterface strategy;

  @BeforeEach
  void setup() {
    entity = mock(Entity.class);
    strategy = new NoneControlStrategy();
  }

  @Test
  void update_doesNothing_noInteractionWithEntity() {
    strategy.update(entity);
    verifyNoInteractions(entity);
  }
}
