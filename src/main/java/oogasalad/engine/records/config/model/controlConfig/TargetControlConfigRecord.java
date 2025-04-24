package oogasalad.engine.records.config.model.controlConfig;

import java.util.Optional;
import oogasalad.engine.records.config.model.controlConfig.targetStrategy.TargetCalculationConfigInterface;

public record TargetControlConfigRecord(
    String pathFindingStrategy,
    TargetCalculationConfigInterface targetCalculationConfig
) implements ControlConfigInterface {
  @Override
  public Optional<String> getPathFindingStrategy() {
    return Optional.of(pathFindingStrategy);
  }
}
