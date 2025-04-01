package oogasalad.engine.model;

import java.util.List;

/**
 * A strategy that determines how to handle collisions.
 *
 * @author Will He
 */
public class CollisionRule {

  private String entityTypeA;
  private String modeA;
  private String entityTypeB;
  private String modeB;
  private List<String> eventsA;
  private List<String> eventsB;

  public CollisionRule() {}

  public String getEntityTypeA() {
    return entityTypeA;
  }

  public void setEntityTypeA(String entityTypeA) {
    this.entityTypeA = entityTypeA;
  }

  public String getModeA() {
    return modeA;
  }

  public void setModeA(String modeA) {
    this.modeA = modeA;
  }

  public String getEntityTypeB() {
    return entityTypeB;
  }

  public void setEntityTypeB(String entityTypeB) {
    this.entityTypeB = entityTypeB;
  }

  public String getModeB() {
    return modeB;
  }

  public void setModeB(String modeB) {
    this.modeB = modeB;
  }

  public List<String> getEventsA() {
    return eventsA;
  }

  public void setEventsA(List<String> eventsA) {
    this.eventsA = eventsA;
  }

  public List<String> getEventsB() {
    return eventsB;
  }

  public void setEventsB(List<String> eventsB) {
    this.eventsB = eventsB;
  }
}
