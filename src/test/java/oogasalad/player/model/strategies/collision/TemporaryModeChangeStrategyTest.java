package oogasalad.player.model.strategies.collision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.engine.records.CollisionContextRecord.StrategyAppliesTo;
import oogasalad.engine.records.config.model.ModeChangeInfo;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TemporaryModeChangeStrategyTest {

  // Test wrote using Claude 3.5 Sonnet with new IntelliJ code assist
  private TemporaryModeChangeStrategy strategy;
  private GameMapInterface mockGameMap;
  private Entity mockEntity1;
  private Entity mockEntity2;
  private GameState mockGameState;
  private Map<Entity, ModeChangeInfo> activeModeChanges;

  private static final String ENTITY_TYPE = "ghost";
  private static final String TEMP_MODE = "scared";
  private static final String TRANSITION_MODE = "returning";
  private static final double DURATION = 10.0;
  private static final double TRANSITION_TIME = 2.0;
  private static final String ORIGINAL_MODE = "chase";
  private static final double CURRENT_TIME = 5.0;

  @BeforeEach
  void setUp() {
    strategy = new TemporaryModeChangeStrategy(ENTITY_TYPE, TEMP_MODE, TRANSITION_MODE, DURATION,
        TRANSITION_TIME);
    mockGameMap = mock(GameMapInterface.class);
    mockEntity1 = mock(Entity.class);
    mockEntity2 = mock(Entity.class);
    mockGameState = mock(GameState.class);
    activeModeChanges = new HashMap<>();

    // Setup entity placements
    EntityPlacement mockPlacement1 = mock(EntityPlacement.class);
    EntityPlacement mockPlacement2 = mock(EntityPlacement.class);
    when(mockEntity1.getEntityPlacement()).thenReturn(mockPlacement1);
    when(mockEntity2.getEntityPlacement()).thenReturn(mockPlacement2);

    // Setup game map
    when(mockGameMap.getActiveModeChanges()).thenReturn(activeModeChanges);
    when(mockGameState.getTimeElapsed()).thenReturn(CURRENT_TIME);

    // Setup initial modes
    when(mockEntity1.getEntityPlacement().getMode()).thenReturn(ORIGINAL_MODE);
    when(mockEntity2.getEntityPlacement().getMode()).thenReturn(ORIGINAL_MODE);
  }

  @Test
  void handleCollision_ChangesMatchingEntityModes() {
    // Setup entities of correct type
    when(mockEntity1.getEntityPlacement().getTypeString()).thenReturn(ENTITY_TYPE);
    when(mockEntity2.getEntityPlacement().getTypeString()).thenReturn(ENTITY_TYPE);

    List<Entity> entities = new ArrayList<>();
    entities.add(mockEntity1);
    entities.add(mockEntity2);
    when(mockGameMap.iterator()).thenReturn(entities.iterator());

    CollisionContextRecord context = new CollisionContextRecord(
        mockEntity1,
        mockEntity2,
        mockGameMap,
        mockGameState,
        StrategyAppliesTo.ENTITY1
    );

    strategy.handleCollision(context);

    // Verify both entities had their modes changed
    verify(mockEntity1.getEntityPlacement()).setMode(TEMP_MODE);
    verify(mockEntity2.getEntityPlacement()).setMode(TEMP_MODE);

    // Verify mode changes were recorded
    ModeChangeInfo expectedInfo = new ModeChangeInfo(ORIGINAL_MODE, TRANSITION_MODE,
        CURRENT_TIME + DURATION, CURRENT_TIME + DURATION - TRANSITION_TIME);

    assertModeChangeInfo(mockEntity1, expectedInfo);
    assertModeChangeInfo(mockEntity2, expectedInfo);
  }

  @Test
  void handleCollision_OnlyChangesMatchingType() {
    // Setup one entity of matching type, one of different type
    when(mockEntity1.getEntityPlacement().getTypeString()).thenReturn(ENTITY_TYPE);
    when(mockEntity2.getEntityPlacement().getTypeString()).thenReturn("different");

    List<Entity> entities = new ArrayList<>();
    entities.add(mockEntity1);
    entities.add(mockEntity2);
    when(mockGameMap.iterator()).thenReturn(entities.iterator());

    CollisionContextRecord context = new CollisionContextRecord(
        mockEntity1,
        mockEntity2,
        mockGameMap,
        mockGameState,
        StrategyAppliesTo.ENTITY1
    );

    strategy.handleCollision(context);

    // Verify only matching entity had mode changed
    verify(mockEntity1.getEntityPlacement()).setMode(TEMP_MODE);
    verify(mockEntity2.getEntityPlacement(), never()).setMode(any());
  }

  @Test
  void handleCollision_UpdatesExistingModeChange() {
    when(mockEntity1.getEntityPlacement().getTypeString()).thenReturn(ENTITY_TYPE);

    // Setup existing mode change
    String previousOriginalMode = "scatter";
    ModeChangeInfo previousInfo = new ModeChangeInfo(previousOriginalMode, TRANSITION_MODE,
        CURRENT_TIME - 1, CURRENT_TIME - 2);
    activeModeChanges.put(mockEntity1, previousInfo);

    List<Entity> entities = new ArrayList<>();
    entities.add(mockEntity1);
    when(mockGameMap.iterator()).thenReturn(entities.iterator());

    CollisionContextRecord context = new CollisionContextRecord(
        mockEntity1,
        mockEntity2,
        mockGameMap,
        mockGameState,
        StrategyAppliesTo.ENTITY1
    );

    strategy.handleCollision(context);

    // Verify mode change was updated with previous original mode
    ModeChangeInfo expectedInfo = new ModeChangeInfo(previousOriginalMode, TRANSITION_MODE,
        CURRENT_TIME + DURATION, CURRENT_TIME + DURATION - TRANSITION_TIME);

    assertModeChangeInfo(mockEntity1, expectedInfo);
  }

  private void assertModeChangeInfo(Entity entity, ModeChangeInfo expectedInfo) {
    ModeChangeInfo actualInfo = activeModeChanges.get(entity);
    assertEquals(expectedInfo.originalMode(), actualInfo.originalMode());
    assertEquals(expectedInfo.transitionMode(), actualInfo.transitionMode());
    assertEquals(expectedInfo.transitionTime(), actualInfo.transitionTime());
  }
}
