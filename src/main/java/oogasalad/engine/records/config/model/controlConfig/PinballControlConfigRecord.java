package oogasalad.engine.records.config.model.controlConfig;

/**
 * Configuration record for the PinballLaunchControlStrategy.
 * Defines parameters for the pinball-like launching behavior.
 */
public record PinballControlConfigRecord(
    double maxLaunchForce,
    double chargeRate,
    double friction,
    double maxDistance
) implements ControlConfigInterface {

  /**
   * Creates a new PinballControlConfigRecord with the specified parameters.
   *
   * @param maxLaunchForce the maximum force that can be applied during a launch
   * @param chargeRate     the rate at which charge accumulates while holding a direction key
   * @param friction       the friction coefficient that reduces velocity over time
   * @param maxDistance    the maximum distance the entity can travel in a single launch
   */
  public PinballControlConfigRecord {
    // Parameter validation
    if (maxLaunchForce <= 0) {
      throw new IllegalArgumentException("Max launch force must be positive");
    }
    if (chargeRate <= 0) {
      throw new IllegalArgumentException("Charge rate must be positive");
    }
    if (friction < 0 || friction > 1) {
      throw new IllegalArgumentException("Friction must be between 0 and 1");
    }
    if (maxDistance <= 0) {
      throw new IllegalArgumentException("Max distance must be positive");
    }
  }
}