package oogasalad.engine.model;

import oogasalad.engine.records.config.model.collisionevent.CollisionEvent;
import oogasalad.engine.records.config.model.collisionevent.ConsumeCollisionEvent;
import oogasalad.engine.records.config.model.collisionevent.UpdateLivesCollisionEvent;
import oogasalad.engine.records.config.model.collisionevent.UpdateScoreCollisionEvent;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollisionRuleTest {

  // TODO: might want to add tests to either setting nulls or change getting null events to return optional

  @Test
  void setEntityTypeA_validValue_returnsCorrectly() {
    CollisionRule rule = new CollisionRule();
    rule.setEntityTypeA("Pacman");
    assertEquals("Pacman", rule.getEntityTypeA());
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
    rule.setEntityTypeB("Ghost");
    assertEquals("Ghost", rule.getEntityTypeB());
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
    List<CollisionEvent> events = List.of(new ConsumeCollisionEvent(),
        new UpdateLivesCollisionEvent(5));
    rule.setEventsA(events);
    assertEquals(events, rule.getEventsA());
  }

  @Test
  void setEventsB_validList_returnsCorrectly() {
    CollisionRule rule = new CollisionRule();
    List<CollisionEvent> events = List.of(new UpdateLivesCollisionEvent(1));
    rule.setEventsB(events);
    assertEquals(events, rule.getEventsB());
  }

  @Test
  void toString_validValues_returnsFormattedString() {
    CollisionRule rule = new CollisionRule();
    rule.setEntityTypeA("Pacman");
    rule.setModeA("PoweredUp");
    rule.setEntityTypeB("Ghost");
    rule.setModeB("Default");
    rule.setEventsA(List.of(new UpdateScoreCollisionEvent(5)));
    rule.setEventsB(List.of(new ConsumeCollisionEvent()));

    String expected = "(Pacman:PoweredUp) ↔ (Ghost:Default) | A: [UpdateScoreCollisionEvent[amount=5]], B: [ConsumeCollisionEvent[]]";
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
