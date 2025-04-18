package oogasalad.engine.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import oogasalad.engine.records.config.model.CollisionEventInterface;
import oogasalad.engine.records.config.model.collisionevent.ConsumeCollisionEventRecord;
import oogasalad.engine.records.config.model.collisionevent.UpdateLivesCollisionEventRecord;
import oogasalad.engine.records.config.model.collisionevent.UpdateScoreCollisionEventRecord;
import org.junit.jupiter.api.Test;

class CollisionRuleTest {

  // TODO: might want to add tests to either setting nulls or change getting null events to return optional

  @Test
  void setEntityTypeA_validValue_returnsCorrectly() {
    CollisionRule rule = new CollisionRule();
    rule.setEntityA("Pacman");
    assertEquals("Pacman", rule.getEntityA());
  }

  @Test
  void setModeA_validValue_returnsCorrectly() {
    CollisionRule rule = new CollisionRule();
    rule.setModeA("PoweredUp");
    assertEquals("PoweredUp", rule.getModeA());
  }

  @Test
  void setEntityTypeB_validValue_returnsCorrectly() {
    CollisionRule rule = new CollisionRule();
    rule.setEntityB("Ghost");
    assertEquals("Ghost", rule.getEntityB());
  }

  @Test
  void setModeB_validValue_returnsCorrectly() {
    CollisionRule rule = new CollisionRule();
    rule.setModeB("Default");
    assertEquals("Default", rule.getModeB());
  }

  @Test
  void setEventsA_validList_returnsCorrectly() {
    CollisionRule rule = new CollisionRule();
    List<CollisionEventInterface> events = List.of(new ConsumeCollisionEventRecord(),
        new UpdateLivesCollisionEventRecord(5));
    rule.setEventsA(events);
    assertEquals(events, rule.getEventsA());
  }

  @Test
  void setEventsB_validList_returnsCorrectly() {
    CollisionRule rule = new CollisionRule();
    List<CollisionEventInterface> events = List.of(new UpdateLivesCollisionEventRecord(1));
    rule.setEventsB(events);
    assertEquals(events, rule.getEventsB());
  }

  @Test
  void toString_validValues_returnsFormattedString() {
    CollisionRule rule = new CollisionRule();
    rule.setEntityA("Pacman");
    rule.setModeA("PoweredUp");
    rule.setEntityB("Ghost");
    rule.setModeB("Default");
    rule.setEventsA(List.of(new UpdateScoreCollisionEventRecord(5)));
    rule.setEventsB(List.of(new ConsumeCollisionEventRecord()));

    String expected = """
        (Type Pacman: Mode PoweredUp) ↔ (Type Ghost: Mode Default)
        Events A: [UpdateScoreCollisionEventRecord[amount=5]]
        Events B: [ConsumeCollisionEventRecord[]]""";
    assertEquals(expected, rule.toString());
  }

  @Test
  void toString_withNulls_doesNotThrowException() {
    CollisionRule rule = new CollisionRule(); // All fields null
    rule.setEventsA(Collections.emptyList());
    rule.setEventsB(Collections.emptyList());

    assertDoesNotThrow(rule::toString);
  }
}
