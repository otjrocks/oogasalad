package oogasalad.engine.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import oogasalad.engine.model.GameState.HudComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateImplTest {

  private GameStateImpl gameState;

  @BeforeEach
  void setUp() {
    gameState = new GameStateImpl(5);
  }

  @Test
  void updateScore_addSubScore_updatesScoreCorrectly() {
    gameState.updateScore(10);
    assertEquals(10, gameState.getScore());

    gameState.updateScore(-3);
    assertEquals(7, gameState.getScore());
  }

  @Test
  void updateLives_addSubLives_updatesLivesCorrectly() {
    gameState.updateLives(-1);
    assertEquals(4, gameState.getLives());

    gameState.updateLives(2);
    assertEquals(6, gameState.getLives());
  }

  @Test
  void addHudComponent_newHudComponent_addsHudComponentCorrectly() {
    HudComponent hud = new MockHudComponent("HealthBar");
    gameState.addHudComponent(hud);

    List<HudComponent> components = gameState.getHudComponents();
    assertEquals(1, components.size());
    assertEquals("HealthBar", ((MockHudComponent) components.getFirst()).name());
  }

  @Test
  void getHudComponents_returnsDefensiveHudComponent_hudComponentReturnedCorrectlyEvenWithAttemptedChange() {
    gameState.addHudComponent(new MockHudComponent("Ammo"));

    List<HudComponent> copy = gameState.getHudComponents();
    copy.clear();

    assertEquals(1, gameState.getHudComponents().size());
  }

  @Test
  void resetState_defaultCall_resetsValuesRemoveComponentsCorrectly() {
    gameState.updateScore(50);
    gameState.updateLives(-2);
    gameState.addHudComponent(new MockHudComponent("Timer"));

    gameState.resetState();

    assertEquals(0, gameState.getScore());
    assertEquals(0, gameState.getLives());
    assertTrue(gameState.getHudComponents().isEmpty());
  }

  private record MockHudComponent(String name) implements HudComponent, java.io.Serializable {

  }
}
