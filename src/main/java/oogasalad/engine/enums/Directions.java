package oogasalad.engine.enums;

public class Directions {

  public enum Direction {
    U(0, -1, 0),
    R(1, 0, 90),
    D(0, 1, 180),
    L(-1, 0, 270),
    NONE(0, 0, null); // No direction, no angle

    private final int dx;
    private final int dy;
    private final Integer angle; // Use Integer to allow null

    Direction(int dx, int dy, Integer angle) {
      this.dx = dx;
      this.dy = dy;
      this.angle = angle;
    }

    public int getDx() {
      return dx;
    }

    public int getDy() {
      return dy;
    }

    public Integer getAngle() {
      return angle;
    }

    public boolean isNone() {
      return this == NONE;
    }

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
