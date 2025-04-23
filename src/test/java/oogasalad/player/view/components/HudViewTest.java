package oogasalad.player.view.components;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import oogasalad.player.model.GameStateInterface;
import oogasalad.player.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HudViewTest extends DukeApplicationTest {

  private HudView hudView;
  private GameStateInterface mockGameState;
  private GameView mockGameView;
  private Runnable mockReturnToMenu;

  @Override
  public void start(Stage stage) {
    mockGameState = mock(GameStateInterface.class);
    mockGameView = mock(GameView.class);
    mockReturnToMenu = mock(Runnable.class);

    hudView = new HudView(mockGameState, mockGameView, mockReturnToMenu);

    runAsJFXAction(() -> {
      Scene scene = new Scene(hudView, 800, 600);
      stage.setScene(scene);
      stage.show();
    });
  }



  @BeforeEach
  public void setUp() {
    reset(mockGameState, mockGameView, mockReturnToMenu);
  }

  @Test
  void hudView_initialize_initializeScoreAndLives() {
    // Ensure the labels are present and initialized with the default values
    Label scoreLabel = lookup("Score: 0").query();
    Label livesLabel = lookup("Lives: 0").query();

    assertNotNull(scoreLabel);
    assertNotNull(livesLabel);
    assertEquals("Score: 0", scoreLabel.getText());
    assertEquals("Lives: 0", livesLabel.getText());
  }


  @Test
  void update_updateLivesAndScore_displayUpdatedScoreAndLives() {
    when(mockGameState.getScore()).thenReturn(10);
    when(mockGameState.getLives()).thenReturn(2);

    runAsJFXAction(() -> hudView.update(mockGameState));

    Label scoreLabel = lookup("Score: 10").query();
    Label livesLabel = lookup("Lives: 2").query();

    assertEquals("Score: 10", scoreLabel.getText());
    assertEquals("Lives: 2", livesLabel.getText());
  }

  @Test
  void playButton_setOnAction_resumesGame() {
    StackPane mockRoot = mock(StackPane.class);

    when(mockGameView.getRoot()).thenReturn(mockRoot);

    Button playButton = lookup("▶").queryButton();

    clickOn(playButton);
    verify(mockGameView).resumeGame();
    verify(mockRoot).requestFocus();
  }

  @Test
  void pauseButton_setOnAction_pauseGame() {
    StackPane mockRoot = mock(StackPane.class);

    when(mockGameView.getRoot()).thenReturn(mockRoot);

    Button pauseButton = lookup("⏸").queryButton();

    clickOn(pauseButton);
    verify(mockGameView).pauseGame();
    verify(mockRoot).requestFocus();
  }

  @Test
  void returnToMenuButton_setOnAction_returnToMenu() {
    Button returnButton = lookup("RETURN TO MENU").queryButton();

    clickOn(returnButton);
    verify(mockReturnToMenu).run();
  }


}
