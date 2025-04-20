package oogasalad.player.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.StackPane;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.constants.GameConfig;
import oogasalad.player.controller.GameLoopController;

/**
 * The main game view of the player. Primarily encapsulates the game map view.
 *
 * @author Owen Jennings
 */
public class GameView {

  public static final int GAME_VIEW_WIDTH = GameConfig.WIDTH - 2 * GameConfig.MARGIN;
  public static final int GAME_VIEW_HEIGHT = GameConfig.HEIGHT - 2 * GameConfig.MARGIN;

  private final StackPane myRoot;
  private final GameLoopController myGameLoopController;
  private final Label endLabel = new Label();
  private final Button restartLevelButton = new Button();
  private final Button nextLevelButton = new Button();
  private final Button resetButton = new Button();

  /**
   * Create the game view.
   *
   * @param gameContext The game context for this view.
   * @param configModel The config model for this view.
   * @param levelIndex  The index of the level that is displayed on this view.
   */
  public GameView(GameContextRecord gameContext, ConfigModelRecord configModel, int levelIndex) {
    myRoot = new StackPane();
    GameMapView myGameMapView = new GameMapView(gameContext, configModel);
    myRoot.setPrefSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    myRoot.setMinSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    myRoot.setMaxSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    myRoot.getChildren().add(myGameMapView.getCanvas());
    myRoot.getStyleClass().add("game-view");
    myRoot.setFocusTraversable(true);
    boolean isFinalLevel = levelIndex >= configModel.levels().size() - 1;

    myGameLoopController = new GameLoopController(gameContext, myGameMapView,
        configModel.levels().get(levelIndex));
    myGameMapView.setGameLoopController(myGameLoopController);
    setUpEndMessage();
    myGameMapView.setEndGameCallback(won -> showEndMessage(won, isFinalLevel));
  }

  /**
   * Return the javafx root element that is modified by this view class.
   *
   * @return A StackPane which is added to by this view class.
   */
  public StackPane getRoot() {
    return myRoot;
  }

  private void setUpEndMessage() {
    configureEndNode(endLabel, "end-label", null);
    StackPane.setAlignment(endLabel, Pos.CENTER);

    configureEndNode(restartLevelButton, "end-button",
        LanguageManager.getMessage("RESTART_LEVEL"));
    configureEndNode(nextLevelButton, "end-button",
        LanguageManager.getMessage("NEXT_LEVEL"));
    configureEndNode(resetButton, "end-button",
        LanguageManager.getMessage("RESET_GAME"));
  }

  private void configureEndNode(Node node, String styleClass, String text) {
    node.setVisible(false);
    node.getStyleClass().add(styleClass);
    if (node instanceof Labeled && text != null) {
      ((Labeled) node).setText(text);
    }
    myRoot.getChildren().add(node);
  }

  private void showEndMessage(boolean gameWon, boolean isFinalLevel) {
    if (gameWon) {
      if (isFinalLevel) {
        endLabel.setText(LanguageManager.getMessage("GAME_WON"));
      } else {
        endLabel.setText(LanguageManager.getMessage("LEVEL_PASSED"));
      }
    } else {
      endLabel.setText(LanguageManager.getMessage("GAME_OVER"));
    }

    endLabel.setVisible(true);
    nextLevelButton.setVisible(gameWon && !isFinalLevel);
    restartLevelButton.setVisible(!gameWon && !isFinalLevel);
    resetButton.setVisible(gameWon && isFinalLevel);
  }


  /**
   * Sets the action to be executed when the restart button is clicked.
   *
   * <p>This allows external components (such as {@link GamePlayerView}) to define what
   * should happen when the player chooses to restart the current level. The provided
   * {@link Runnable} will be invoked when the restart button is activated.</p>
   *
   * @param action a {@code Runnable} representing the restart behavior
   */
  public void setRestartAction(Runnable action) {
    restartLevelButton.setOnAction(e -> action.run());
  }

  /**
   * Sets the action to be executed when the next level button is clicked.
   *
   * @param action a {@code Runnable} representing the restart behavior
   */
  public void setNextLevelAction(Runnable action) {
    nextLevelButton.setOnAction(e -> action.run());
  }

  /**
   * Sets the action to be executed when the reset button is clicked.
   *
   * <p>This allows external components (such as {@link GamePlayerView}) to define what
   * should happen when the player chooses to restart the current level. The provided
   * {@link Runnable} will be invoked when the restart button is activated.</p>
   *
   * @param action a {@code Runnable} representing the restart behavior
   */
  public void setResetAction(Runnable action) {
    resetButton.setOnAction(e -> action.run());
  }

  /**
   * Pause the game loop from the game loop controller associated with this game view.
   */
  public void pauseGame() {
    myGameLoopController.pauseGame();
  }

  /**
   * Resume the game loop from the game loop controller associated with this game view.
   */
  public void resumeGame() {
    myGameLoopController.resumeGame();
  }
}
