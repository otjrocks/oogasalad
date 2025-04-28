package oogasalad.player.view.components;

import static oogasalad.engine.utility.constants.GameConfig.ELEMENT_SPACING;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.player.model.GameStateInterface;
import oogasalad.player.view.GameView;

/**
 * A reusable HUD component that displays score/lives on one row and controls on the next.
 *
 * @author Luke Fu
 */
public class HudView extends VBox {

  private final Label scoreLabel;
  private final Label livesLabel;
  private final Label highScoreLabel;
  private int currentHighScore;


  /**
   * Constructs HUDView according to state, game view, and runnable
   */
  public HudView(GameStateInterface gameState, GameView gameView, Runnable onReturnToMenu) {
    super(ELEMENT_SPACING);

    // Row 1: Score + Lives + High Score
    scoreLabel = new Label();
    livesLabel = new Label();
    highScoreLabel = new Label();
    HBox statsRow = new HBox(ELEMENT_SPACING, scoreLabel, livesLabel, highScoreLabel);
    String style = "hud-container";
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
      gameView.getRoot().requestFocus();
    });

    pauseButton.setOnAction(e -> {
      gameView.pauseGame();
      gameView.getRoot().requestFocus();
    });

    returnButton.setOnAction(e -> onReturnToMenu.run());

    scoreLabel.setId("scoreLabel");
    livesLabel.setId("livesLabel");
    highScoreLabel.setId("highScoreLabel");

    playButton.setId("playButton");
    pauseButton.setId("pauseButton");
    returnButton.setId("returnToMenuButton");

    HBox controlRow = new HBox(ELEMENT_SPACING, playButton, pauseButton, returnButton);
    controlRow.getStyleClass().add(style);

    this.getChildren().addAll(statsRow, controlRow);
    update(gameState);
  }

  /**
   * Updates the HUD score label and lives label
   */
  public void update(GameStateInterface gameState) {
    scoreLabel.setText(
        String.format(LanguageManager.getMessage("SCORE_LABEL"), gameState.getScore()));
    livesLabel.setText(
        String.format(LanguageManager.getMessage("LIVES_LABEL"), gameState.getLives()));
    updateHighScoreLabelIfNecessary(gameState);
  }

  private void updateHighScoreLabelIfNecessary(GameStateInterface gameState) {
    if (gameState.getHighScore() > currentHighScore) {
      highScoreLabel.setText(
          String.format(LanguageManager.getMessage("HIGH_SCORE_LABEL"), gameState.getHighScore()));
      currentHighScore = gameState.getHighScore();
    }
  }
}
