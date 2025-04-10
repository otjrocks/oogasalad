package oogasalad.engine.records.newconfig;

import java.util.List;

public record CollisionConfig(String entityA, List<Integer> modeA, String entityB,
                              List<Integer> modeB, List<String> eventsA, List<String> eventsB) {

}
