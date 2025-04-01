package oogasalad.engine.model;

public class Tiles {
  private String[] layout;  // Each string is a row of tiles

  public Tiles() {}

  public Tiles(String[] layout) {
    this.layout = layout;
  }

  public String[] getLayout() {
    return layout;
  }

  public void setLayout(String[] layout) {
    this.layout = layout;
  }
}
