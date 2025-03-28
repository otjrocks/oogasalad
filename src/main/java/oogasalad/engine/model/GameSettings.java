package oogasalad.engine.model;

public class GameSettings {
  private double gameSpeed;
  private int startingLives;
  private int initialScore;
  private String edgePolicy;

  public int getInitialScore() {
    return initialScore;
  }

  public void setInitialScore(int initialScore) {
    this.initialScore = initialScore;
  }

  public double getGameSpeed() {
    return gameSpeed;
  }

  public void setGameSpeed(double gameSpeed) {
    this.gameSpeed = gameSpeed;
  }

  public int getStartingLives() {
    return startingLives;
  }

  public void setStartingLives(int startingLives) {
    this.startingLives = startingLives;
  }

  public String getEdgePolicy() {
    return edgePolicy;
  }

  public void setEdgePolicy(String edgePolicy) {
    this.edgePolicy = edgePolicy;
  }

  public GameSettings() {}
}
