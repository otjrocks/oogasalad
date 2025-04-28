package oogasalad.player.model.strategies.control;


import static org.mockito.Mockito.*;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.JumpControlConfigRecord;
import oogasalad.player.controller.GameInputManager;
import oogasalad.player.model.GameMapInterface;
import org.junit.Before;
import org.junit.Test;

public class JumpControlStrategyTest {

  // Test assisted with ChatGPT
  private GameInputManager mockInputManager;
  private GameMapInterface mockGameMap;
  private EntityPlacement mockEntityPlacement;
  private JumpControlConfigRecord mockControlConfig;
  private JumpControlStrategy jumpControlStrategy;

  @Before
  public void setUp() {
    // Mock dependencies
    mockInputManager = mock(GameInputManager.class);
    mockGameMap = mock(GameMapInterface.class);
    mockEntityPlacement = mock(EntityPlacement.class);
    mockControlConfig = mock(JumpControlConfigRecord.class);

    // Mock jump force and gravity values
    when(mockControlConfig.jumpforce()).thenReturn(10.0);
    when(mockControlConfig.gravity()).thenReturn(1.0);

    // Instantiate JumpControlStrategy
    jumpControlStrategy = new JumpControlStrategy(mockInputManager, mockGameMap,
        mockEntityPlacement, mockControlConfig);
  }

  @Test
  public void update_whenUpKeyIsPressed_jumpForceIsApplied() {
    // Setup input manager to simulate Up key press
    when(mockInputManager.isMovingUp()).thenReturn(true);
    when(mockEntityPlacement.isJumping()).thenReturn(
        false); // Ensure the entity is not jumping initially

    // Update the strategy
    jumpControlStrategy.update(null);

    // Verify the jump force is applied
    verify(mockEntityPlacement).setVerticalVelocity(-10.0); // Jump force is negative
    verify(mockEntityPlacement).setJumping(true);
  }

  @Test
  public void update_whenEntityIsFalling_gravityIsApplied() {
    // Simulate entity is falling
    when(mockEntityPlacement.getVerticalVelocity()).thenReturn(-5.0);

    // Update the strategy
    jumpControlStrategy.update(null);

    // Verify gravity is applied for a falling velocity
    verify(mockEntityPlacement).setVerticalVelocity(-5.0 + 1.0 * 0.8); // LOW_JUMP_MULTIPLIER is 0.8
  }

  @Test
  public void update_whenEntityIsStationary_gravityIsApplied() {
    // Simulate entity is stationary (not moving vertically)
    when(mockEntityPlacement.getVerticalVelocity()).thenReturn(0.0);

    // Update the strategy
    jumpControlStrategy.update(null);

    // Verify gravity is applied for stationary velocity
    verify(mockEntityPlacement).setVerticalVelocity(0.0 + 1.0); // Gravity is added
  }
}
