package oogasalad.engine.model;

import java.util.List;
import oogasalad.engine.records.config.model.collisionevent.CollisionEvent;

/**
 * Represents a rule for handling a collision between two entity types, each potentially in a
 * specific mode. Specifies a list of events that should be triggered for each entity upon
 * collision.
 * <p>
 * Used in the game engine to define conditional, mode-based collision behavior. Example: "Pacman"
 * in "PoweredUp" mode colliding with "Ghost" in "Default" mode could trigger "EatGhost" for Pacman
 * and "Die" for Ghost.
 * <p>
 * Author: Will He
 */
public class CollisionRule {

  private String entityTypeA;
  private String modeA;
  private String entityTypeB;
  private String modeB;
  private List<CollisionEvent> eventsA;
  private List<CollisionEvent> eventsB;

  /**
   * Represents a rule that defines the behavior of a collision between two entities in a game. Each
   * rule specifies the types and modes of the two entities involved in the collision, as well as
   * the events triggered for each entity upon collision.
   *
   * @param entityTypeA the type of the first entity involved in the collision
   * @param modeA       the mode of the first entity during the collision
   * @param entityTypeB the type of the second entity involved in the collision
   * @param modeB       the mode of the second entity during the collision
   * @param eventsA     a list of collision events triggered for the first entity upon collision
   * @param eventsB     a list of collision events triggered for the second entity upon collision
   */
  public CollisionRule(String entityTypeA, String modeA, String entityTypeB, String modeB,
      List<CollisionEvent> eventsA, List<CollisionEvent> eventsB) {
    this.entityTypeA = entityTypeA;
    this.modeA = modeA;
    this.entityTypeB = entityTypeB;
    this.modeB = modeB;
    this.eventsA = eventsA;
    this.eventsB = eventsB;
  }

  /**
   * Empty constructor for use by authoring environment to create a blank new collision rule.
   */
  public CollisionRule() {
    // needs empty constructor
  }

  /**
   * Returns the type of the first entity involved in the collision.
   *
   * @return the string identifier for entity A
   */
  public String getEntityTypeA() {
    return entityTypeA;
  }

  /**
   * Sets the type of the first entity involved in the collision.
   *
   * @param entityTypeA the string identifier for entity A
   */
  public void setEntityTypeA(String entityTypeA) {
    this.entityTypeA = entityTypeA;
  }

  /**
   * Returns the mode of the first entity involved in the collision.
   *
   * @return the mode string for entity A
   */
  public String getModeA() {
    return modeA;
  }

  /**
   * Sets the mode of the first entity involved in the collision.
   *
   * @param modeA the mode string for entity A
   */
  public void setModeA(String modeA) {
    this.modeA = modeA;
  }

  /**
   * Returns the type of the second entity involved in the collision.
   *
   * @return the string identifier for entity B
   */
  public String getEntityTypeB() {
    return entityTypeB;
  }

  /**
   * Sets the type of the second entity involved in the collision.
   *
   * @param entityTypeB the string identifier for entity B
   */
  public void setEntityTypeB(String entityTypeB) {
    this.entityTypeB = entityTypeB;
  }

  /**
   * Returns the mode of the second entity involved in the collision.
   *
   * @return the mode string for entity B
   */
  public String getModeB() {
    return modeB;
  }

  /**
   * Sets the mode of the second entity involved in the collision.
   *
   * @param modeB the mode string for entity B
   */
  public void setModeB(String modeB) {
    this.modeB = modeB;
  }

  /**
   * Returns the list of events to apply to the first entity (A) after collision.
   *
   * @return a list of event names for entity A
   */
  public List<CollisionEvent> getEventsA() {
    return eventsA;
  }

  /**
   * Sets the list of events to apply to the first entity (A) after collision.
   *
   * @param eventsA a list of event names for entity A
   */
  public void setEventsA(List<CollisionEvent> eventsA) {
    this.eventsA = eventsA;
  }

  /**
   * Returns the list of events to apply to the second entity (B) after collision.
   *
   * @return a list of event names for entity B
   */
  public List<CollisionEvent> getEventsB() {
    return eventsB;
  }

  /**
   * Sets the list of events to apply to the second entity (B) after collision.
   *
   * @param eventsB a list of event names for entity B
   */
  public void setEventsB(List<CollisionEvent> eventsB) {
    this.eventsB = eventsB;
  }

  @Override
  public String toString() {
    return String.format("(%s:%s) ↔ (%s:%s) | A: %s, B: %s",
        entityTypeA, modeA, entityTypeB, modeB,
        String.join(",", eventsA.toString()),
        String.join(",", eventsB.toString()));
  }

}
