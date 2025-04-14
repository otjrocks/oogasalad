package oogasalad.engine.model.controlConfig.targetStrategy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "targetCalculationStrategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TargetEntityConfig.class, name = "TargetEntity"),
    @JsonSubTypes.Type(value = TargetAheadOfEntityConfig.class, name = "TargetAheadOfEntity"),
    @JsonSubTypes.Type(value = TargetEntityWithTrapConfig.class, name = "TargetEntityWithTrap"),
    @JsonSubTypes.Type(value = TargetLocationConfig.class, name = "TargetLocation")
})
public interface TargetCalculationConfig { }
