package oogasalad.engine.model.api;

import static org.junit.jupiter.api.Assertions.*;

import oogasalad.engine.model.strategies.collision.CollisionStrategy;
import oogasalad.engine.model.strategies.collision.ConsumeStrategy;
import oogasalad.engine.model.strategies.collision.StopStrategy;
import oogasalad.engine.model.strategies.collision.UpdateLivesStrategy;
import oogasalad.engine.model.strategies.collision.UpdateScoreStrategy;
import org.junit.jupiter.api.Test;

class StrategyFactoryTest {

  // I used ChatGPT to assist in writing these tests.
//  @Test
//  void createCollisionStrategy_stopStrategy_CorrectStrategy() {
//    CollisionStrategy strategy = StrategyFactory.createCollisionStrategy("Stop");
//    assertInstanceOf(StopStrategy.class, strategy, "Expected a StopStrategy");
//  }

//  @Test
//  void createCollisionStrategy_updateLivesStrategy_CorrectStrategy() {
//    CollisionStrategy strategy = StrategyFactory.createCollisionStrategy("UpdateLives(3)");
//    assertInstanceOf(UpdateLivesStrategy.class, strategy, "Expected an UpdateLivesStrategy");
//  }

//  @Test
//  void createCollisionStrategy_updateScoreStrategy_CorrectStrategy() {
//    CollisionStrategy strategy = StrategyFactory.createCollisionStrategy("UpdateScore(10)");
//    assertInstanceOf(UpdateScoreStrategy.class, strategy, "Expected an UpdateScoreStrategy");
//  }

//  @Test
//  void createCollisionStrategy_defaultConsumeStrategy_CorrectStrategy() {
//    CollisionStrategy strategy = StrategyFactory.createCollisionStrategy("UnknownStrategy");
//    assertInstanceOf(ConsumeStrategy.class, strategy, "Expected a ConsumeStrategy");
//  }

//  @Test
//  void createCollisionStrategy_InvalidParameterFormat_ThrowException() {
//    assertThrows(NumberFormatException.class,
//        () -> StrategyFactory.createCollisionStrategy("UpdateLives(InvalidNumber)"));
//  }

}
