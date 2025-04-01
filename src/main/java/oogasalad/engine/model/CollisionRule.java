package oogasalad.engine.model;

/**
 * A strategy that determines how to handle collisions.
 *
 * @author Will He
 */
public class CollisionRule {

  private String entityTypeA;
  private String entityTypeB;
  private String strategy;

  /**
   * Get the entity type b.
   *
   * @return The string representing the type of entity b.
   */
  public String getEntityTypeB() {
    return entityTypeB;
  }

  /**
   * Set the string representing entity type b.
   *
   * @param entityTypeB The string you wish to set type b to.
   */
  public void setEntityTypeB(String entityTypeB) {
    this.entityTypeB = entityTypeB;
  }

  /**
   * Get the entity type of entity A.
   *
   * @return The string representing the entity type.
   */
  public String getEntityTypeA() {
    return entityTypeA;
  }

  /**
   * Set the string representing the type of entity A.
   *
   * @param entityTypeA The string you wish to set type A to.
   */
  public void setEntityTypeA(String entityTypeA) {
    this.entityTypeA = entityTypeA;
  }

  /**
   * Get the strategy string for this collision
   *
   * @return A string representing the strategy to use between these two entities.
   */
  public String getStrategy() {
    return strategy;
  }

  /**
   * Set the strategy to be used between two entity types.
   *
   * @param strategy The string representing the strategy you wish to be used.
   */
  public void setStrategy(String strategy) {
    this.strategy = strategy;
  }
}
