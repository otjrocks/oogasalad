package oogasalad.engine.model.controlConfig.targetStrategy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A interface used to translate the json configuration information into java records.
 *
 * @author Will He, Owen Jennings
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "targetCalculationStrategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TargetEntityConfigRecord.class, name = "TargetEntity"),
    @JsonSubTypes.Type(value = TargetAheadOfEntityConfigRecord.class, name = "TargetAheadOfEntity"),
    @JsonSubTypes.Type(value = TargetEntityWithTrapConfigRecord.class, name = "TargetEntityWithTrap"),
    @JsonSubTypes.Type(value = TargetLocationConfigRecord.class, name = "TargetLocation")
})
public interface TargetCalculationConfigInterface { }
