package oogasalad.engine.model;

public class CollisionRule {
  private String entityTypeA;
  private String entityTypeB;
  private String strategy;

  public String getEntityTypeB() {
    return entityTypeB;
  }

  public void setEntityTypeB(String entityTypeB) {
    this.entityTypeB = entityTypeB;
  }

  public String getEntityTypeA() {
    return entityTypeA;
  }

  public void setEntityTypeA(String entityTypeA) {
    this.entityTypeA = entityTypeA;
  }

  public String getStrategy() {
    return strategy;
  }

  public void setStrategy(String strategy) {
    this.strategy = strategy;
  }

  public CollisionRule() {}
}
