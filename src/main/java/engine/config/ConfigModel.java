package engine.config;

import engine.model.CollisionRule;
import engine.model.EntityData;
import engine.model.GameSettings;
import engine.model.MetaData;
import java.util.List;

public class ConfigModel {
  public MetaData metadata;
  public GameSettings settings;
  public List<EntityData> entityConfigs;
  public List<CollisionRule> collisionRules;
  public String winCondition;

  public ConfigModel() {} // Required for Jackson
}
