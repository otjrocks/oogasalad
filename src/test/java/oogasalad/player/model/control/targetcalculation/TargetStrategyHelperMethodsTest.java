package oogasalad.player.model.control.targetcalculation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

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
  public void setUp() {
    mockMap = mock(GameMap.class);
    mockEntity = mock(Entity.class);
    mockPlacement = mock(EntityPlacement.class);
  }

  @Test
  void findFirstEntityOfType_validEntity_returnEntity() {
    String targetType = "Enemy";
    when(mockPlacement.getTypeString()).thenReturn("Enemy");
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    Iterator<Entity> mockIterator = Collections.singletonList(mockEntity).iterator();
    when(mockMap.iterator()).thenReturn(mockIterator);

    Optional<Entity> result = TargetStrategyHelperMethods.findFirstEntityOfType(mockMap, targetType);

    assertTrue(result.isPresent());
    assertEquals(mockEntity, result.get());
  }

  @Test
  void findFirstEntityOfType_invalidEntity_returnEmpty() {
    String targetType = "Enemy";
    when(mockPlacement.getTypeString()).thenReturn("Friend");
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    Iterator<Entity> mockIterator = Collections.singletonList(mockEntity).iterator();
    when(mockMap.iterator()).thenReturn(mockIterator);

    Optional<Entity> result = TargetStrategyHelperMethods.findFirstEntityOfType(mockMap, targetType);

    assertFalse(result.isPresent());
  }

  @Test
  void findFirstEntityOfType_multipleEntities_returnFirstEntity() {
    Entity mockEnemy1 = mock(Entity.class);
    Entity mockEnemy2 = mock(Entity.class);
    EntityPlacement placement1 = mock(EntityPlacement.class);
    EntityPlacement placement2 = mock(EntityPlacement.class);

    when(mockEnemy1.getEntityPlacement()).thenReturn(placement1);
    when(mockEnemy2.getEntityPlacement()).thenReturn(placement2);
    when(placement1.getTypeString()).thenReturn("Enemy");
    when(placement2.getTypeString()).thenReturn("Enemy");

    Iterator<Entity> mockIterator = Arrays.asList(mockEnemy1, mockEnemy2).iterator();
    when(mockMap.iterator()).thenReturn(mockIterator);

    Optional<Entity> result = TargetStrategyHelperMethods.findFirstEntityOfType(mockMap, "Enemy");

    assertTrue(result.isPresent());
    assertEquals(mockEnemy1, result.get());
  }

  @Test
  void validateAndGetTargetType_validConfig_returnsType() {
    Map<String, Object> config = new HashMap<>();
    config.put("targetType", "Enemy");

    String result = TargetStrategyHelperMethods.validateAndGetTargetType(config, "targetType");
    assertEquals("Enemy", result);
  }

  @Test
  void validateAndGetTargetType_missingKey_throwsException() {
    Map<String, Object> config = new HashMap<>();

    Exception exception = assertThrows(TargetStrategyException.class, () ->
        TargetStrategyHelperMethods.validateAndGetTargetType(config, "targetType")
    );

    assertEquals("Type targetType is required", exception.getMessage());
  }

  @Test
  void validateAndGetTargetType_nullValue_returnsStringNull() {
    Map<String, Object> config = new HashMap<>();
    config.put("targetType", null);

    Exception exception = assertThrows(TargetStrategyException.class, () ->
        TargetStrategyHelperMethods.validateAndGetTargetType(config, "targetType")
    );

    assertEquals("Type targetType is required", exception.getMessage());
  }

  @Test
  void validateAndGetTargetType_nonStringValue_returnsConvertedString() {
    Map<String, Object> config = new HashMap<>();
    config.put("targetType", 123);  // Integer

    String result = TargetStrategyHelperMethods.validateAndGetTargetType(config, "targetType");
    assertEquals("123", result);
  }

}
