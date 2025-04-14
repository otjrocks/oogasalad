package oogasalad.engine.records.newconfig;

import java.util.List;
import oogasalad.engine.records.newconfig.model.collisionevent.CollisionEvent;

public record CollisionConfig(String entityA, List<Integer> modeA, String entityB,
                              List<Integer> modeB, List<CollisionEvent> eventsA, List<CollisionEvent> eventsB) {

}
