package oogasalad.player.view.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.model.GameState;

import static oogasalad.engine.config.GameConfig.ELEMENT_SPACING;

/**
 * A view for displaying HUD elements such as score and lives.
 */
public class HudView extends HBox {

  private final Label scoreLabel;
  private final Label livesLabel;
  private final GameState gameState;

  public HudView(GameState gameState) {
    this.gameState = gameState;

    scoreLabel = new Label();
    livesLabel = new Label();

    this.setSpacing(ELEMENT_SPACING);
    this.getStyleClass().add("hud-container");

    update(); // initialize values

    this.getChildren().addAll(scoreLabel, livesLabel);
  }

  public void update() {
    scoreLabel.setText(String.format(LanguageManager.getMessage("SCORE_LABEL"), gameState.getScore()));
    livesLabel.setText(String.format(LanguageManager.getMessage("LIVES_LABEL"), gameState.getLives()));
  }
}
