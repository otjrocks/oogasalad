package oogasalad.engine.controller;

import javafx.scene.Group;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.view.AuthoringView;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.engine.view.SplashScreenView;
import oogasalad.player.controller.GameInputManager;
import oogasalad.player.model.GameState;
import oogasalad.player.model.api.GameStateFactory;
import oogasalad.player.view.GameScreenView;
import oogasalad.player.view.GameSelectorView;

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
  private SplashScreenView mySplashScreenView = null;
  private AuthoringView myAuthoringView = null;
  private GameSelectorView myGameSelectorView = null;

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
    showSplashScreen();
  }

  /**
   * Show the splash screen view, if it is not already being displayed.
   */
  public void showSplashScreen() {
    if (mySplashScreenView == null) {
      mySplashScreenView = new SplashScreenView(this);
    }
    if (!myRoot.getChildren().contains(mySplashScreenView.getRoot())) {
      myRoot.getChildren().add(mySplashScreenView.getRoot());
    }
  }

  /**
   * Hide the splash screen view.
   */
  public void hideSplashScreen() {
    if (myRoot.getChildren().contains(mySplashScreenView.getRoot())) {
      myRoot.getChildren().remove(mySplashScreenView.getRoot());
    } else {
      LoggingManager.LOGGER.warn(
          "Attempted to hide the splash screen, even though it wasn't being displayed.");
    }
  }

  /**
   * Show the game selector view if it is not already being displayed.
   */
  public void showGameSelectorView() {
    if (myGameSelectorView == null) {
      myGameSelectorView = new GameSelectorView(this);
    }
    if (!myRoot.getChildren().contains(myGameSelectorView.getRoot())) {
      myGameSelectorView.resetUploadSection();
      myRoot.getChildren().add(myGameSelectorView.getRoot());
    }
  }

  /**
   * Hide the game selector view.
   */
  public void hideGameSelectorView() {
    if (myRoot.getChildren().contains(myGameSelectorView.getRoot())) {
      myRoot.getChildren().remove(myGameSelectorView.getRoot());
    } else {
      LoggingManager.LOGGER.warn(
          "Attempted to hide the game selector screen, even though it wasn't being displayed.");
    }
  }

  /**
   * Show the game player view if it is not already being displayed.
   *
   * @param gameFolderName name of game folder to create
   * @param randomized     if levels should be randomized
   */
  public void showGamePlayerView(String gameFolderName, boolean randomized) {
    GameState myGameState = GameStateFactory.createFromConfig("data/games/" + gameFolderName);
    GameScreenView myGameScreenView = new GameScreenView(this, myGameState, gameFolderName, randomized);
    myInputManager.getRoot().getChildren().add(myGameScreenView.getRoot());
  }

  /**
   * Show the authoring environment view if not already displayed
   */
  public void showAuthoringView() {
    if (myAuthoringView == null) {
      AuthoringModel model = new AuthoringModel();
      myAuthoringView = new AuthoringView();
      AuthoringController controller = new AuthoringController(model, myAuthoringView);
      myAuthoringView.setController(controller);
    }
    if (!myInputManager.getRoot().getChildren().contains(myAuthoringView.getNode())) {
      myInputManager.getRoot().getChildren().add(myAuthoringView.getNode());
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
