package oogasalad.player.model.control.targetcalculation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.exceptions.TargetStrategyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TargetStrategyHelperMethodsTest {

  private GameMap mockMap;
  private Entity mockEntity;
  private EntityPlacement mockPlacement;

  @BeforeEach
  void setUp() {
    mockMap = mock(GameMap.class);
    mockEntity = mock(Entity.class);
    mockPlacement = mock(EntityPlacement.class);
  }

  @Test
  void findFirstEntityOfType_entityMatches_returnsEntity() {
    when(mockPlacement.getTypeString()).thenReturn("Enemy");
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockMap.iterator()).thenReturn(Collections.singletonList(mockEntity).iterator());

    Optional<Entity> result = TargetStrategyHelperMethods.findFirstEntityOfType(mockMap, "Enemy");

    assertTrue(result.isPresent());
    assertEquals(mockEntity, result.get());
  }

  @Test
  void findFirstEntityOfType_noMatch_returnsEmpty() {
    when(mockPlacement.getTypeString()).thenReturn("Friend");
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockMap.iterator()).thenReturn(Collections.singletonList(mockEntity).iterator());

    Optional<Entity> result = TargetStrategyHelperMethods.findFirstEntityOfType(mockMap, "Enemy");

    assertFalse(result.isPresent());
  }

  @Test
  void validateAndGetKeyString_valid_returnsValue() {
    Map<String, Object> config = Map.of("targetType", "Enemy");
    String result = TargetStrategyHelperMethods.validateAndGetKeyString(config, "targetType");
    assertEquals("Enemy", result);
  }

  @Test
  void validateAndGetKeyString_missingKey_throwsException() {
    Map<String, Object> config = new HashMap<>();
    Exception e = assertThrows(TargetStrategyException.class,
        () -> TargetStrategyHelperMethods.validateAndGetKeyString(config, "targetType"));
    assertEquals("Type targetType is required", e.getMessage());
  }

  @Test
  void validateAndGetKeyString_nullValue_throwsException() {
    Map<String, Object> config = new HashMap<>();
    config.put("targetType", null);
    Exception e = assertThrows(TargetStrategyException.class,
        () -> TargetStrategyHelperMethods.validateAndGetKeyString(config, "targetType"));
    assertEquals("Type targetType is required", e.getMessage());
  }

  @Test
  void validateAndGetKeyString_nonString_returnsToStringValue() {
    Map<String, Object> config = Map.of("targetType", 123);
    String result = TargetStrategyHelperMethods.validateAndGetKeyString(config, "targetType");
    assertEquals("123", result);
  }

  @Test
  void validateAndGetKeyInt_validInt_returnsIntValue() {
    Map<String, Object> config = Map.of("tilesAhead", 5);
    int result = TargetStrategyHelperMethods.validateAndGetKeyInt(config, "tilesAhead");
    assertEquals(5, result);
  }

  @Test
  void validateAndGetKeyInt_validDouble_returnsIntValue() {
    Map<String, Object> config = Map.of("tilesAhead", 4.7);
    int result = TargetStrategyHelperMethods.validateAndGetKeyInt(config, "tilesAhead");
    assertEquals(4, result);
  }

  @Test
  void validateAndGetKeyInt_nonNumber_throwsException() {
    Map<String, Object> config = Map.of("tilesAhead", "five");
    Exception e = assertThrows(TargetStrategyException.class,
        () -> TargetStrategyHelperMethods.validateAndGetKeyInt(config, "tilesAhead"));
    assertEquals("tilesAhead must be a number convertible to int", e.getMessage());
  }

  @Test
  void validateAndGetKeyInt_missingKey_throwsException() {
    Map<String, Object> config = new HashMap<>();
    Exception e = assertThrows(TargetStrategyException.class,
        () -> TargetStrategyHelperMethods.validateAndGetKeyInt(config, "tilesAhead"));
    assertEquals("Type tilesAhead is required", e.getMessage());
  }

  @Test
  void calcTargetPosition_validTarget_returnsNewPosition() {
    when(mockEntity.getEntityDirection()).thenReturn(Direction.R);
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockPlacement.getX()).thenReturn(3.0);
    when(mockPlacement.getY()).thenReturn(2.0);

    when(mockMap.isValidPosition(5, 2)).thenReturn(true);
    when(mockMap.isNotBlocked("Enemy", 5, 2)).thenReturn(true);

    int[] result = TargetStrategyHelperMethods.calcTargetPosition(mockMap, mockEntity, "Enemy", 2);
    assertArrayEquals(new int[]{5, 2}, result);
  }

  @Test
  void calcTargetPosition_invalidPosition_returnsCurrentPosition() {
    when(mockEntity.getEntityDirection()).thenReturn(Direction.U);
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockPlacement.getX()).thenReturn(2.0);
    when(mockPlacement.getY()).thenReturn(2.0);

    when(mockMap.isValidPosition(2, 0)).thenReturn(false);  // target blocked

    int[] result = TargetStrategyHelperMethods.calcTargetPosition(mockMap, mockEntity, "Enemy", 2);
    assertArrayEquals(new int[]{2, 2}, result);
  }

  @Test
  void calcTargetPosition_nullDirection_returnsCurrentPosition() {
    when(mockEntity.getEntityDirection()).thenReturn(null);
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockPlacement.getX()).thenReturn(1.0);
    when(mockPlacement.getY()).thenReturn(1.0);

    int[] result = TargetStrategyHelperMethods.calcTargetPosition(mockMap, mockEntity, "Enemy", 3);
    assertArrayEquals(new int[]{1, 1}, result);
  }
}
