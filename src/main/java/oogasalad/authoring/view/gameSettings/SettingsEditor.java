package oogasalad.authoring.view.gameSettings;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import oogasalad.engine.utility.LanguageManager;

public class SettingsEditor {

  private final Spinner<Double> gameSpeedSpinner;
  private final Spinner<Integer> livesSpinner;
  private final Spinner<Integer> scoreSpinner;
  private final GridPane root;

  public SettingsEditor(double gameSpeed, int startingLives, int initialScore) {
    root = new GridPane();
    root.setHgap(10);
    root.setVgap(10);
    root.setPadding(new Insets(10));

    gameSpeedSpinner = new Spinner<>(0.5, 3.0, gameSpeed, 0.1);
    gameSpeedSpinner.setEditable(true);

    livesSpinner = new Spinner<>(1, 10, startingLives, 1);
    livesSpinner.setEditable(true);

    scoreSpinner = new Spinner<>(0, 1000, initialScore, 50);
    scoreSpinner.setEditable(true);

    root.add(new Label(LanguageManager.getMessage("GAME_SPEED")), 0, 0);
    root.add(gameSpeedSpinner, 1, 0);
    root.add(new Label(LanguageManager.getMessage("STARTING_LIVES")), 0, 1);
    root.add(livesSpinner, 1, 1);
    root.add(new Label(LanguageManager.getMessage("INITIAL_SCORE")), 0, 2);
    root.add(scoreSpinner, 1, 2);
  }

  public Node getNode() {
    return root;
  }

  public double getGameSpeed() {
    return gameSpeedSpinner.getValue();
  }

  public int getStartingLives() {
    return livesSpinner.getValue();
  }

  public int getInitialScore() {
    return scoreSpinner.getValue();
  }

  public void update(double gameSpeed, int startingLives, int initialScore) {
    gameSpeedSpinner.getValueFactory().setValue(gameSpeed);
    livesSpinner.getValueFactory().setValue(startingLives);
    scoreSpinner.getValueFactory().setValue(initialScore);
  }

}
