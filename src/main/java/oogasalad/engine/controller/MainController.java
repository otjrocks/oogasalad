package oogasalad.engine.controller;

import javafx.scene.Group;
import javafx.stage.Stage;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.GameStateImpl;
import oogasalad.engine.view.SplashScreenView;
import oogasalad.player.view.GameScreenView;

/**
 * The main controller of the game engine. This class controls the interactions between the model
 * and view and transitions between views for the game engine.
 *
 * @author Owen Jennings
 */
public class MainController {

  private final Group myRoot;
  private final Stage myStage;
  private final GameInputManager myInputManager;
  private final GameStateImpl myGameState;
  private SplashScreenView mySplashScreenView = null;
  private GameScreenView myGameScreenView = null;

  /**
   * Create a main controller for the program.
   *
   * @param stage The stage of the main application.
   * @param root  The root element of the main application.
   */
  public MainController(Stage stage, Group root) {
    myRoot = root;
    myStage = stage;
    myInputManager = new GameInputManager(stage.getScene(), myRoot);
    myGameState = new GameStateImpl(3);
    showSplashScreen();
  }

  /**
   * Show the splash screen view, if it is not already being displayed.
   */
  public void showSplashScreen() {
    if (mySplashScreenView == null) {
      mySplashScreenView = new SplashScreenView(this);
    }
    if (!myRoot.getChildren().contains(mySplashScreenView)) {
      myRoot.getChildren().add(mySplashScreenView);
    }
  }

  /**
   * Hide the splash screen view.
   */
  public void hideSplashScreen() {
    if (myRoot.getChildren().contains(mySplashScreenView)) {
      myRoot.getChildren().remove(mySplashScreenView);
    } else {
      LoggingManager.LOGGER.warn(
          "Attempted to hide the splash screen, even though it wasn't being displayed.");
    }
  }

  /**
   * Show the game player view if it is not already being displayed.
   */
  public void showGamePlayerView() {
    if (myGameScreenView == null) {
      myGameScreenView = new GameScreenView(this, myGameState);
    }
    if (!myRoot.getChildren().contains(myGameScreenView)) {
      myInputManager.getRoot().getChildren().add(myGameScreenView);
    }
  }

  /**
   * Get the main stage of this controller.
   *
   * @return The stage associated with this controller.
   */
  public Stage getStage() {
    return myStage;
  }

  /**
   * Get the input manager initialized in MainController.
   */
  public GameInputManager getInputManager() {
    return myInputManager;
  }
}
