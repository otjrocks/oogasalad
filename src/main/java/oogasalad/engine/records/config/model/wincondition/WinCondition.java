package oogasalad.engine.records.config.model.wincondition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An interface used to parse information about a win condition from the configuration file.
 *
 * @author Owen Jennings
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SurviveForTimeCondition.class, name = "SurviveForTime"),
})
public interface WinCondition { }
