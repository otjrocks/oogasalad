package oogasalad.player.view.components;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.model.GameState;
import oogasalad.player.view.GameView;

import static oogasalad.engine.config.GameConfig.ELEMENT_SPACING;

/**
 * A reusable HUD component that displays score/lives on one row and controls on the next.
 * @author Luke Fu
 */
public class HudView extends VBox {

  private final Label scoreLabel;
  private final Label livesLabel;
  private final String style = "hud-container";
  /**
   * Constructs HUDView according to state, game view, and runnable
   */
  public HudView(GameState gameState, GameView gameView, Runnable onReturnToMenu) {
    super(ELEMENT_SPACING);

    // Row 1: Score + Lives
    scoreLabel = new Label();
    livesLabel = new Label();
    HBox statsRow = new HBox(ELEMENT_SPACING, scoreLabel, livesLabel);
    statsRow.getStyleClass().add(style);

    // Row 2: Buttons
    Button playButton = new Button("▶");
    Button pauseButton = new Button("⏸");
    Button returnButton = new Button(LanguageManager.getMessage("RETURN_TO_MENU"));

    playButton.setFocusTraversable(false);
    pauseButton.setFocusTraversable(false);
    returnButton.setFocusTraversable(false);

    playButton.setOnAction(e -> {
      gameView.resumeGame();
      gameView.requestFocus();
    });

    pauseButton.setOnAction(e -> {
      gameView.pauseGame();
      gameView.requestFocus();
    });

    returnButton.setOnAction(e -> onReturnToMenu.run());

    HBox controlRow = new HBox(ELEMENT_SPACING, playButton, pauseButton, returnButton);
    controlRow.getStyleClass().add(style);

    this.getChildren().addAll(statsRow, controlRow);
    update(gameState);
  }

  /**
   * Updates the HUD score label and lives label
   */
  public void update(GameState gameState) {
    scoreLabel.setText(String.format(LanguageManager.getMessage("SCORE_LABEL"), gameState.getScore()));
    livesLabel.setText(String.format(LanguageManager.getMessage("LIVES_LABEL"), gameState.getLives()));
  }
}
