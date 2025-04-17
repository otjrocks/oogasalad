package oogasalad.player.model.api;

import static org.junit.jupiter.api.Assertions.*;

import oogasalad.player.model.strategies.collision.CollisionStrategyInterface;
import oogasalad.player.model.strategies.collision.ConsumeStrategy;
import oogasalad.player.model.strategies.collision.StopStrategy;
import oogasalad.player.model.strategies.collision.UpdateLivesStrategy;
import oogasalad.player.model.strategies.collision.UpdateScoreStrategy;
import oogasalad.engine.records.config.model.collisionevent.ConsumeCollisionEventRecord;
import oogasalad.engine.records.config.model.collisionevent.StopCollisionEventRecord;
import oogasalad.engine.records.config.model.collisionevent.UpdateLivesCollisionEventRecord;
import oogasalad.engine.records.config.model.collisionevent.UpdateScoreCollisionEventRecord;
import org.junit.jupiter.api.Test;

class CollisionStrategyFactoryTest {

  // I used ChatGPT to assist in writing these tests.
  @Test
  void createCollisionStrategy_stopStrategy_CorrectStrategy() {
    CollisionStrategyInterface strategy = CollisionStrategyFactory.createCollisionStrategy(new StopCollisionEventRecord());
    assertInstanceOf(StopStrategy.class, strategy, "Expected a StopStrategy");
  }

  @Test
  void createCollisionStrategy_updateLivesStrategy_CorrectStrategy() {
    CollisionStrategyInterface strategy = CollisionStrategyFactory.createCollisionStrategy(
        new UpdateLivesCollisionEventRecord(5));
    assertInstanceOf(UpdateLivesStrategy.class, strategy, "Expected an UpdateLivesStrategy");
  }

  @Test
  void createCollisionStrategy_updateScoreStrategy_CorrectStrategy() {
    CollisionStrategyInterface strategy = CollisionStrategyFactory.createCollisionStrategy(
        new UpdateScoreCollisionEventRecord(5));
    assertInstanceOf(UpdateScoreStrategy.class, strategy, "Expected an UpdateScoreStrategy");
  }

  @Test
  void createCollisionStrategy_ConsumeStrategy_CorrectStrategy() {
    CollisionStrategyInterface strategy = CollisionStrategyFactory.createCollisionStrategy(
        new ConsumeCollisionEventRecord());
    assertInstanceOf(ConsumeStrategy.class, strategy, "Expected a ConsumeStrategy");
  }

}
