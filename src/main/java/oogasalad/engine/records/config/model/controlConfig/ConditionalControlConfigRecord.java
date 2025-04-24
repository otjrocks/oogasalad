package oogasalad.engine.records.config.model.controlConfig;

import java.util.Optional;
import oogasalad.engine.records.config.model.controlConfig.targetStrategy.TargetCalculationConfigInterface;

public record ConditionalControlConfigRecord(
    int radius,
    String pathFindingStrategyInRadius,
    String pathFindingStrategyOutRadius,
    TargetCalculationConfigInterface targetCalculationConfig
) implements ControlConfigInterface {
  @Override
  public Optional<String> getPathFindingStrategyInRadius() {
    return Optional.of(pathFindingStrategyInRadius);
  }

  @Override
  public Optional<String> getPathFindingStrategyOutRadius() {
    return Optional.of(pathFindingStrategyOutRadius);
  }

  @Override
  public Optional<Integer> getRadius() {
    return Optional.of(radius);
  }
}
