package oogasalad.engine.records.config.model.losecondition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An interface used to parse information about a lose condition from the configuration file.
 *
 * @author Austin Huang
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = LivesBasedCondition.class, name = "LivesBased")
})
public interface LoseCondition { }
