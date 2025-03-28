package oogasalad.engine.config;

import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.model.EntityData;
import oogasalad.engine.model.GameSettings;
import oogasalad.engine.model.MetaData;
import java.util.List;

public class ConfigModel {
  public MetaData metadata;
  public GameSettings settings;
  public List<EntityData> entityConfigs;
  public List<CollisionRule> collisionRules;
  public String winCondition;

  public ConfigModel() {}
}
