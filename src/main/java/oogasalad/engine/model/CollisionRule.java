package oogasalad.engine.model;

import java.util.List;
import oogasalad.engine.records.config.model.CollisionEventInterface;

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

  private String entityA;
  private String modeA;
  private String entityB;
  private String modeB;
  private List<CollisionEventInterface> eventsA;
  private List<CollisionEventInterface> eventsB;

  /**
   * Represents a rule that defines the behavior of a collision between two entities in a game. Each
   * rule specifies the types and modes of the two entities involved in the collision, as well as
   * the events triggered for each entity upon collision.
   *
   * @param entityA the type of the first entity involved in the collision
   * @param modeA   the mode of the first entity during the collision
   * @param entityB the type of the second entity involved in the collision
   * @param modeB   the mode of the second entity during the collision
   * @param eventsA a list of collision events triggered for the first entity upon collision
   * @param eventsB a list of collision events triggered for the second entity upon collision
   */
  public CollisionRule(String entityA, String modeA, String entityB, String modeB,
      List<CollisionEventInterface> eventsA, List<CollisionEventInterface> eventsB) {
    this.entityA = entityA;
    this.modeA = modeA;
    this.entityB = entityB;
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
  public String getEntityA() {
    return entityA;
  }

  /**
   * Sets the type of the first entity involved in the collision.
   *
   * @param entityA the string identifier for entity A
   */
  public void setEntityA(String entityA) {
    this.entityA = entityA;
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
  public String getEntityB() {
    return entityB;
  }

  /**
   * Sets the type of the second entity involved in the collision.
   *
   * @param entityB the string identifier for entity B
   */
  public void setEntityB(String entityB) {
    this.entityB = entityB;
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
  public List<CollisionEventInterface> getEventsA() {
    return eventsA;
  }

  /**
   * Sets the list of events to apply to the first entity (A) after collision.
   *
   * @param eventsA a list of event names for entity A
   */
  public void setEventsA(List<CollisionEventInterface> eventsA) {
    this.eventsA = eventsA;
  }

  /**
   * Returns the list of events to apply to the second entity (B) after collision.
   *
   * @return a list of event names for entity B
   */
  public List<CollisionEventInterface> getEventsB() {
    return eventsB;
  }

  /**
   * Sets the list of events to apply to the second entity (B) after collision.
   *
   * @param eventsB a list of event names for entity B
   */
  public void setEventsB(List<CollisionEventInterface> eventsB) {
    this.eventsB = eventsB;
  }

  @Override
  public String toString() {
    return String.format("(Type %s: Mode %s) ↔ (Type %s: Mode %s)\nEvents A: %s\nEvents B: %s",
        entityA, modeA, entityB, modeB,
        String.join(",", eventsA.toString()),
        String.join(",", eventsB.toString()));
  }

}
