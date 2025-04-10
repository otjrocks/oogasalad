package oogasalad.engine.model.strategies.collision;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import oogasalad.engine.model.GameState;
import oogasalad.engine.records.CollisionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpdateScoreStrategyTest {

  private GameState gameState;
  private CollisionContext context;

  @BeforeEach
  void setUp() {
    gameState = mock(GameState.class);
    context = mock(CollisionContext.class);

    when(context.gameState()).thenReturn(gameState);
  }

  // TODO: I'm not sure what else to test about this?

  @Test
  void handleCollision_positiveIncrement_increasesScore() {
    UpdateScoreStrategy strategy = new UpdateScoreStrategy(2);

    strategy.handleCollision(context);

    verify(gameState).updateScore(2);
  }

}
