package oogasalad.player.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.StackPane;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.player.controller.GameLoopController;

/**
 * The main game view of the player. Primarily encapsulates the game map view.
 *
 * @author Owen Jennings
 */
public class GameView extends StackPane {

  public static final int GAME_VIEW_WIDTH = GameConfig.WIDTH - 2 * GameConfig.MARGIN;
  public static final int GAME_VIEW_HEIGHT = GameConfig.HEIGHT - 2 * GameConfig.MARGIN;

  private final GameLoopController myGameLoopController;
  private final Label endLabel = new Label();
  private final Button restartButton = new Button();
  private final Button nextLevelButton = new Button();

  /**
   * Create the game view.
   *
   * @param gameContext The game context for this view.
   * @param configModel The config model for this view.
   * @param levelIndex  The index of the level that is displayed on this view.
   */
  public GameView(GameContextRecord gameContext, ConfigModel configModel, int levelIndex) {
    super();
    GameMapView myGameMapView = new GameMapView(gameContext, configModel);
    this.setPrefSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.setMinSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.setMaxSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.getChildren().add(myGameMapView.getCanvas());
    this.getStyleClass().add("game-view");
    this.setFocusTraversable(true);
    boolean isFinalLevel = levelIndex >= configModel.levels().size() - 1;

    myGameLoopController = new GameLoopController(gameContext, myGameMapView,
        configModel.levels().get(levelIndex));
    myGameMapView.setGameLoopController(myGameLoopController);
    setUpEndMessage();
    myGameMapView.setEndGameCallback(won -> showEndMessage(won, isFinalLevel));
  }

  private void setUpEndMessage() {
    configureEndNode(endLabel, "end-label", null);
    StackPane.setAlignment(endLabel, Pos.CENTER);

    configureEndNode(restartButton, "end-button",
        LanguageManager.getMessage("RESTART_LEVEL"));
    configureEndNode(nextLevelButton, "end-button",
        LanguageManager.getMessage("NEXT_LEVEL"));
  }

  private void configureEndNode(Node node, String styleClass, String text) {
    node.setVisible(false);
    node.getStyleClass().add(styleClass);
    if (node instanceof Labeled && text != null) {
      ((Labeled) node).setText(text);
    }
    this.getChildren().add(node);
  }

  private void showEndMessage(boolean gameWon, boolean isFinalLevel) {
    if (gameWon) {
      if (isFinalLevel) {
        endLabel.setText("🎉 Congrats! 🎉");
      } else {
        endLabel.setText(LanguageManager.getMessage("LEVEL_PASSED"));
      }
    } else {
      endLabel.setText(LanguageManager.getMessage("GAME_OVER"));
    }

    endLabel.setVisible(true);
    nextLevelButton.setVisible(gameWon && !isFinalLevel);
    restartButton.setVisible(!gameWon || isFinalLevel);
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
    restartButton.setOnAction(e -> action.run());
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
