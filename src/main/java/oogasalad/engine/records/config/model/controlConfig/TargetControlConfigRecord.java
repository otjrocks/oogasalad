package oogasalad.engine.records.config.model.controlConfig;

import oogasalad.engine.records.config.model.controlConfig.targetStrategy.TargetCalculationConfigInterface;

public record TargetControlConfigRecord(
    String pathFindingStrategy,
    TargetCalculationConfigInterface targetCalculationConfig
) implements ControlConfigInterface {

}
