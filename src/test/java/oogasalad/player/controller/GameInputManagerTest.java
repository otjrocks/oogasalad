package oogasalad.player.controller;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import oogasalad.player.view.GameScreenView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameInputManagerTest extends DukeApplicationTest {

  private GameInputManager gameInputManager;

  @BeforeEach
  void setUp() {
    // ChatGPT assisted in generating these tests.
    Scene scene = mock(Scene.class);
    Group root = mock(Group.class);
    gameInputManager = new GameInputManager(scene, root);
    GameScreenView gameScreenView = mock(GameScreenView.class);
    gameInputManager.setGameScreenView(gameScreenView);
  }

  @Test
  void isMovingUp_whenKeyReleased_keyShouldNotMoveUp() {
    KeyEvent keyPressedEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false,
        false, false);
    gameInputManager.getRoot().fireEvent(keyPressedEvent);

    keyPressedEvent = new KeyEvent(KeyEvent.KEY_RELEASED, "", "", KeyCode.UP, false, false, false,
        false);
    gameInputManager.getRoot().fireEvent(keyPressedEvent);

    assertFalse(gameInputManager.isMovingUp(),
        "Entity should not be moving up after releasing UP key.");
  }

  @Test
  void removeUpKey_whenRemoveUpKeyCalled_keyShouldNotMoveUp() {
    KeyEvent keyPressedEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false,
        false, false);
    gameInputManager.getRoot().fireEvent(keyPressedEvent);

    gameInputManager.removeUpKey();

    assertFalse(gameInputManager.isMovingUp(),
        "Entity should not be moving up after removeUpKey is called.");
  }

  @Test
  void getGameScreenView_whenGetGameScreenViewCalled_gameScreenViewShouldNotBeNull() {
    assertNotNull(gameInputManager.getGameScreenView(), "GameScreenView should not be null.");
  }
}
