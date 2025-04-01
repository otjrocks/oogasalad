package oogasalad.engine.model;

import java.util.List;

/**
 * Represents a rule for handling a collision between two entity types,
 * each potentially in a specific mode. Specifies a list of events that
 * should be triggered for each entity upon collision.
 *
 * Used in the game engine to define conditional, mode-based collision behavior.
 * Example: "Pacman" in "PoweredUp" mode colliding with "Ghost" in "Default" mode
 * could trigger "EatGhost" for Pacman and "Die" for Ghost.
 *
 * Author: Will He
 */
public class CollisionRule {

  private String entityTypeA;
  private String modeA;
  private String entityTypeB;
  private String modeB;
  private List<String> eventsA;
  private List<String> eventsB;

  /** Default constructor for deserialization or reflection. */
  public CollisionRule() {
    // Empty
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
  public List<String> getEventsA() {
    return eventsA;
  }

  /**
   * Sets the list of events to apply to the first entity (A) after collision.
   *
   * @param eventsA a list of event names for entity A
   */
  public void setEventsA(List<String> eventsA) {
    this.eventsA = eventsA;
  }

  /**
   * Returns the list of events to apply to the second entity (B) after collision.
   *
   * @return a list of event names for entity B
   */
  public List<String> getEventsB() {
    return eventsB;
  }

  /**
   * Sets the list of events to apply to the second entity (B) after collision.
   *
   * @param eventsB a list of event names for entity B
   */
  public void setEventsB(List<String> eventsB) {
    this.eventsB = eventsB;
  }
}
