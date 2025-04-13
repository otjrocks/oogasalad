package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameState;
import oogasalad.engine.records.CollisionContextRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class UpdateLivesStrategyTest {

  private GameState gameState;
  private CollisionContextRecord context;

  @BeforeEach
  void setUp() {
    gameState = mock(GameState.class);
    context = mock(CollisionContextRecord.class);

    when(context.gameState()).thenReturn(gameState);
  }

  // TODO: I'm not sure what else to test about this?

  @Test
  void handleCollision_positiveIncrement_increasesLives() {
    UpdateLivesStrategy strategy = new UpdateLivesStrategy(2);

    strategy.handleCollision(context);

    verify(gameState).updateLives(2);
  }

}
