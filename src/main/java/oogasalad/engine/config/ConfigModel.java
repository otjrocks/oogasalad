package oogasalad.engine.config;

import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.model.EntityData;
import oogasalad.engine.model.GameSettings;
import oogasalad.engine.model.MetaData;
import java.util.List;

public class ConfigModel {
  private MetaData metadata;
  private GameSettings settings;
  private List<EntityData> entityConfigs;
  private List<CollisionRule> collisionRules;
  private String winCondition;

  public ConfigModel() {}

  public MetaData getMetadata() {
    return metadata;
  }

  public void setMetadata(MetaData metadata) {
    this.metadata = metadata;
  }

  public GameSettings getSettings() {
    return settings;
  }

  public void setSettings(GameSettings settings) {
    this.settings = settings;
  }

  public List<EntityData> getEntityConfigs() {
    return entityConfigs;
  }

  public void setEntityConfigs(List<EntityData> entityConfigs) {
    this.entityConfigs = entityConfigs;
  }

  public List<CollisionRule> getCollisionRules() {
    return collisionRules;
  }

  public void setCollisionRules(List<CollisionRule> collisionRules) {
    this.collisionRules = collisionRules;
  }

  public String getWinCondition() {
    return winCondition;
  }

  public void setWinCondition(String winCondition) {
    this.winCondition = winCondition;
  }
}
