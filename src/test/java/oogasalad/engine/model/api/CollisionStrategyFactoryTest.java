package oogasalad.engine.model.api;

import static org.junit.jupiter.api.Assertions.*;

import oogasalad.engine.model.strategies.collision.CollisionStrategy;
import oogasalad.engine.model.strategies.collision.ConsumeStrategy;
import oogasalad.engine.model.strategies.collision.StopStrategy;
import oogasalad.engine.model.strategies.collision.UpdateLivesStrategy;
import oogasalad.engine.model.strategies.collision.UpdateScoreStrategy;
import oogasalad.engine.records.config.model.collisionevent.ConsumeCollisionEvent;
import oogasalad.engine.records.config.model.collisionevent.StopCollisionEvent;
import oogasalad.engine.records.config.model.collisionevent.UpdateLivesCollisionEvent;
import oogasalad.engine.records.config.model.collisionevent.UpdateScoreCollisionEvent;
import org.junit.jupiter.api.Test;

class CollisionStrategyFactoryTest {

  // I used ChatGPT to assist in writing these tests.
  @Test
  void createCollisionStrategy_stopStrategy_CorrectStrategy() {
    CollisionStrategy strategy = CollisionStrategyFactory.createCollisionStrategy(new StopCollisionEvent());
    assertInstanceOf(StopStrategy.class, strategy, "Expected a StopStrategy");
  }

  @Test
  void createCollisionStrategy_updateLivesStrategy_CorrectStrategy() {
    CollisionStrategy strategy = CollisionStrategyFactory.createCollisionStrategy(
        new UpdateLivesCollisionEvent(5));
    assertInstanceOf(UpdateLivesStrategy.class, strategy, "Expected an UpdateLivesStrategy");
  }

  @Test
  void createCollisionStrategy_updateScoreStrategy_CorrectStrategy() {
    CollisionStrategy strategy = CollisionStrategyFactory.createCollisionStrategy(
        new UpdateScoreCollisionEvent(5));
    assertInstanceOf(UpdateScoreStrategy.class, strategy, "Expected an UpdateScoreStrategy");
  }

  @Test
  void createCollisionStrategy_ConsumeStrategy_CorrectStrategy() {
    CollisionStrategy strategy = CollisionStrategyFactory.createCollisionStrategy(
        new ConsumeCollisionEvent());
    assertInstanceOf(ConsumeStrategy.class, strategy, "Expected a ConsumeStrategy");
  }

}
