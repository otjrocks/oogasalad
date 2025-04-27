package oogasalad.engine.utility.constants;

/**
 * The Directions class contains an enumeration of possible directions (U, R, D, L, NONE) with
 * associated properties such as x and y deltas and angles. It provides utility methods to retrieve
 * these properties and to determine a direction from a given angle.
 */
public class Directions {

  /**
   * Enum representing possible directions with associated properties.
   */
  public enum Direction {
    U(0, -1, 0),
    R(1, 0, 90),
    D(0, 1, 180),
    L(-1, 0, 270),
    NONE(0, 0, null);

    private final int dx;
    private final int dy;
    private final Integer angle;

    /**
     * Constructs a Direction with specified x and y deltas and an angle.
     *
     * @param dx    the change in x-coordinate for this direction
     * @param dy    the change in y-coordinate for this direction
     * @param angle the angle associated with this direction, or null if none
     */
    Direction(int dx, int dy, Integer angle) {
      this.dx = dx;
      this.dy = dy;
      this.angle = angle;
    }

    /**
     * Gets the change in x-coordinate for this direction.
     *
     * @return the x delta
     */
    public int getDx() {
      return dx;
    }

    /**
     * Gets the change in y-coordinate for this direction.
     *
     * @return the y delta
     */
    public int getDy() {
      return dy;
    }

    /**
     * Gets the angle associated with this direction.
     *
     * @return the angle, or null if no angle is associated
     */
    public Integer getAngle() {
      return angle;
    }

    /**
     * Checks if this direction is NONE (no direction).
     *
     * @return true if this direction is NONE, false otherwise
     */
    public boolean isNone() {
      return this != NONE;
    }

    /**
     * Determines the direction corresponding to a given angle. If no matching direction is found,
     * returns NONE.
     *
     * @param angle the angle to match, normalized to 0–359 degrees
     * @return the corresponding Direction, or NONE if no match is found
     */
    public static Direction fromAngle(int angle) {
      angle = ((angle % 360) + 360) % 360; // Normalize to 0–359
      for (Direction d : values()) {
        if (d.angle != null && d.angle == angle) {
          return d;
        }
      }
      return NONE;
    }
  }
}
