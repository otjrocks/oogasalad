package oogasalad.engine.records.config;

import java.util.List;
import oogasalad.engine.records.config.model.CollisionEvent;

public record CollisionConfig(String entityA, String modeA, String entityB,
                              String modeB, List<CollisionEvent> eventsA,
                              List<CollisionEvent> eventsB) {

}
