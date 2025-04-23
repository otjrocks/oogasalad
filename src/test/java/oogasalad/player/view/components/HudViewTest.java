//package oogasalad.player.view.components;
//
//import javafx.embed.swing.JFXPanel;
//import javafx.scene.control.Label;
//import javafx.scene.layout.VBox;
//import oogasalad.player.model.GameStateInterface;
//import oogasalad.player.view.GameView;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class HudViewTest {
//
//  private GameStateInterface mockGameState;
//  private GameView mockGameView;
//  private HudView hudView;
//
//  static {
//    new JFXPanel(); // JavaFX init
//  }
//
//  @BeforeEach
//  void setup() {
//    mockGameState = mock(GameStateInterface.class);
//    mockGameView = mock(GameView.class);
//
//    when(mockGameState.getScore()).thenReturn(150);
//    when(mockGameState.getLives()).thenReturn(5);
//
//    hudView = new HudView(mockGameState, mockGameView, () -> {});
//  }
//
//  @Test
//  void testHudViewHasStatsAndControls() {
//    VBox hudRoot = hudView;
//    assertEquals(2, hudRoot.getChildren().size(), "HudView should have 2 rows: stats + controls");
//  }
//
//  @Test
//  void testUpdateReflectsNewGameState() {
//    when(mockGameState.getScore()).thenReturn(200);
//    when(mockGameState.getLives()).thenReturn(1);
//
//    hudView.update(mockGameState);
//
//    Label scoreLabel = (Label) ((VBox) hudView).getChildren().get(0).lookupAll(".label").toArray()[0];
//    assertTrue(scoreLabel.getText().contains("200"), "Score label should reflect updated score");
//
//    Label livesLabel = (Label) ((VBox) hudView).getChildren().get(0).lookupAll(".label").toArray()[1];
//    assertTrue(livesLabel.getText().contains("1"), "Lives label should reflect updated lives");
//  }
//}
