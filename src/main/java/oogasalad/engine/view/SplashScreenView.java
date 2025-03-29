package oogasalad.engine.view;


import javafx.scene.layout.VBox;
import oogasalad.engine.view.components.LanguageSelector;

public class SplashScreenView extends VBox {

  public SplashScreenView() {
    this.getChildren().add(new LanguageSelector().getSelector());
  }
}
