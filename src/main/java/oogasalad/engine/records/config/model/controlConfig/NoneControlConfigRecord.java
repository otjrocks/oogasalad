package oogasalad.engine.records.config.model.controlConfig;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A record used to store if the user chooses "None" as control strategy.
 *
 * @author Owen Jennings
 */
@JsonTypeName("None")
public record NoneControlConfigRecord() implements ControlConfigInterface {

}
