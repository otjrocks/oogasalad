package oogasalad.engine.records.config;

import java.util.List;
import oogasalad.engine.records.config.model.CollisionEventInterface;

public record CollisionConfigRecord(String entityA, String modeA, String entityB,
                                    String modeB, List<CollisionEventInterface> eventsA,
                                    List<CollisionEventInterface> eventsB) {

}
