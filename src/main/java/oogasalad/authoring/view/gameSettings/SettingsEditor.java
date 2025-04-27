package oogasalad.authoring.view.gameSettings;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import oogasalad.engine.utility.LanguageManager;

/**
 * SettingsEditor provides UI components for editing core game settings: game speed, starting lives,
 * and initial score.
 * <p>
 * The editor uses a GridPane layout and provides methods to retrieve or update the current values.
 * <p>
 * Designed to be a modular sub-component of GameSettingsView.
 *
 * @author William He
 */
public class SettingsEditor {

  private final Spinner<Double> gameSpeedSpinner;
  private final Spinner<Integer> livesSpinner;
  private final Spinner<Integer> scoreSpinner;
  private final GridPane root;

  /**
   * Constructs a SettingsEditor with initial game settings values.
   *
   * @param gameSpeed     initial game speed
   * @param startingLives initial number of lives
   * @param initialScore  initial score
   */
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

  /**
   * Returns the root Node containing the settings fields.
   *
   * @return the Node representing the editor layout
   */
  public Node getNode() {
    return root;
  }

  /**
   * Gets the currently selected game speed.
   *
   * @return the game speed value
   */
  public double getGameSpeed() {
    return gameSpeedSpinner.getValue();
  }

  /**
   * Gets the currently selected starting lives.
   *
   * @return the starting lives value
   */
  public int getStartingLives() {
    return livesSpinner.getValue();
  }

  /**
   * Gets the currently selected initial score.
   *
   * @return the initial score value
   */
  public int getInitialScore() {
    return scoreSpinner.getValue();
  }

  /**
   * Updates the fields to reflect new game settings values.
   *
   * @param gameSpeed     the new game speed
   * @param startingLives the new starting lives
   * @param initialScore  the new initial score
   */
  public void update(double gameSpeed, int startingLives, int initialScore) {
    gameSpeedSpinner.getValueFactory().setValue(gameSpeed);
    livesSpinner.getValueFactory().setValue(startingLives);
    scoreSpinner.getValueFactory().setValue(initialScore);
  }
}
