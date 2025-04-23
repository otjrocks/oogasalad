//package oogasalad.player.view;
//
//import javafx.embed.swing.JFXPanel;
//import javafx.scene.layout.StackPane;
//import oogasalad.engine.controller.MainController;
//import oogasalad.player.model.GameStateInterface;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class GamePlayerViewTest {
//
//  private MainController mockMainController;
//  private GameStateInterface mockGameState;
//  private GamePlayerView gamePlayerView;
//
//  static {
//    new JFXPanel(); // JavaFX init
//  }
//
//  @BeforeEach
//  void setup() {
//    mockMainController = mock(MainController.class);
//    mockGameState = mock(GameStateInterface.class);
//
//    gamePlayerView = new GamePlayerView(mockMainController, mockGameState, "MockGame", false, "src/test/resources/");
//  }
//
//  @Test
//  void testPaneIsNotNullAndHasGameView() {
//    StackPane pane = gamePlayerView.getPane();
//    assertNotNull(pane, "StackPane should not be null");
//    assertFalse(pane.getChildren().isEmpty(), "StackPane should contain GameView root node");
//  }
//
//  @Test
//  void testGameViewIsInitialized() {
//    assertNotNull(gamePlayerView.getGameView(), "GameView should be initialized and not null");
//  }
//}
