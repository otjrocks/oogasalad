package oogasalad.engine.model;

import java.util.List;
import java.util.Map;
import oogasalad.engine.config.ModeConfig;

/**
 * EntityTemplate defines a reusable entity type, including control behavior,
 * image modes, AI strategies, and collision behavior.
 *
 * @author Will He
 */
public class EntityType {

  private String type;
  private String controlType; // e.g. "Keyboard", "TargetEntity"
  private String effect; // e.g. "SpeedBoost(5)"
  private Map<String, ModeConfig> modes; // key = mode name (e.g. "Default", "PoweredUp")
  private List<String> blocks; // which types this entity blocks, e.g. ["Pacman", "Ghost"]
  private Map<String, Object> strategyConfig; // optional parameters for movement strategy

  public EntityType() {}

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getControlType() {
    return controlType;
  }

  public void setControlType(String controlType) {
    this.controlType = controlType;
  }

  public String getEffect() {
    return effect;
  }

  public void setEffect(String effect) {
    this.effect = effect;
  }

  public Map<String, ModeConfig> getModes() {
    return modes;
  }

  public void setModes(Map<String, ModeConfig> modes) {
    this.modes = modes;
  }

  public List<String> getBlocks() {
    return blocks;
  }

  public void setBlocks(List<String> blocks) {
    this.blocks = blocks;
  }

  public Map<String, Object> getStrategyConfig() {
    return strategyConfig;
  }

  public void setStrategyConfig(Map<String, Object> strategyConfig) {
    this.strategyConfig = strategyConfig;
  }
}
